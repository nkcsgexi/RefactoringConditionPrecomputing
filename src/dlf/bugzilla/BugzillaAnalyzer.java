package dlf.bugzilla;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaClient;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaCorePlugin;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaRepositoryConnector;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaTaskAttachmentHandler;
import org.eclipse.mylyn.internal.context.core.InteractionContext;
import org.eclipse.mylyn.internal.context.core.SaxContextContentHandler;
import org.eclipse.mylyn.internal.tasks.core.RepositoryQuery;
import org.eclipse.mylyn.internal.tasks.core.TaskTask;
import org.eclipse.mylyn.internal.tasks.core.sync.SynchronizationSession;
import org.eclipse.mylyn.monitor.core.InteractionEvent;
import org.eclipse.mylyn.tasks.core.IRepositoryPerson;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import dlf.refactoring.precondition.util.XLoggerFactory;


public class BugzillaAnalyzer {
	
	private static final Date START_DATE = startOfYesterday();                                                    

	static final String URL = "https://bugs.eclipse.org/bugs/";
	static final String PRODUCT = "Mylyn";
	static final String BUGZILLA_LOGIN = "FILLIN";
	static final String BUGZILLA_PWD = "FILLIN";
	static final String HTTP_AUTH_LOGIN = null;
	static final String HTTP_AUTH_PWD = null;
	static final Logger logger = XLoggerFactory.GetLogger(BugzillaAnalyzer.class);
	
	static Map<String, Float> knowledgeMap = new HashMap<String, Float>();
	
	public static void dosomething() {

		TaskRepository repository = new TaskRepository(
				BugzillaCorePlugin.CONNECTOR_KIND, URL);

		AuthenticationCredentials credentials = new AuthenticationCredentials(
				BUGZILLA_LOGIN, BUGZILLA_PWD);
		repository.setCredentials(AuthenticationType.REPOSITORY, credentials,
				false);

		if (HTTP_AUTH_LOGIN != null) {
			AuthenticationCredentials webCredentials = new AuthenticationCredentials(
					HTTP_AUTH_LOGIN, HTTP_AUTH_PWD);
			repository.setCredentials(AuthenticationType.HTTP, webCredentials,
					false);
		}

		BugzillaRepositoryConnector connector = new BugzillaRepositoryConnector();
		BugzillaClient client = null;
		try {
			client = connector.getClientManager().getClient(repository,
					new NullProgressMonitor());
		} catch (CoreException ce) {
			ce.printStackTrace();
		}

		try {
			Set<Integer> bugsChangedSinceYesterday = bugsChangedSince(repository, connector);	
			getAttachment(repository, connector, client, bugsChangedSinceYesterday);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private static Date startOfYesterday() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
		c.add(Calendar.DATE, -1000);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		Date startOfYesterday = c.getTime();
		return startOfYesterday;
	}
	
	
	private static Set<Integer> bugsChangedSince(TaskRepository repository, BugzillaRepositoryConnector 
			connector) {
		
		final Set<Integer> bugsChangedSinceYesterday = new HashSet<Integer>();
		
		TaskDataCollector resultCollector = new TaskDataCollector(){
			@Override
			public void accept(TaskData td) {								
				bugsChangedSinceYesterday.add(Integer.valueOf(td.getTaskId()));
			}
		};
		
		ISynchronizationSession session = new SynchronizationSession();//can be null?
		IRepositoryQuery query = bugsChangedIn(repository);			
		connector.performQuery(repository, query, resultCollector, session, new NullProgressMonitor());
		return bugsChangedSinceYesterday;
	}
	

	private static IRepositoryQuery bugsChangedIn(TaskRepository repository) {
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		
		String name = "Query Page Name";
		String queryString = 	"/buglist.cgi?product="+PRODUCT+
								"&chfieldfrom=" + f.format(START_DATE) +
								"&chfieldto=" + "Now";
		
		//if you get a NPE here, try running this in a runtime workspaec
		IRepositoryQuery query = new RepositoryQuery(BugzillaCorePlugin.CONNECTOR_KIND, "my-query");
		query.setUrl(repository.getRepositoryUrl() + queryString);
		query.setSummary(name);
		return query;
	}
	
	static void getAttachment(final TaskRepository repository, BugzillaRepositoryConnector connector,
			BugzillaClient client, Set<Integer> bugIds) throws Exception {
		for(Integer bugId : bugIds) {
			TaskData taskData = connector.getTaskData(repository, bugId.toString(), null);
			BugzillaTaskAttachmentHandler attachmentHandler = (BugzillaTaskAttachmentHandler) connector
					.getTaskAttachmentHandler();
			for(Entry<String, TaskAttribute> entry : taskData.getRoot().getAttributes().entrySet()) {
				if(entry.getKey().startsWith("task.common.attachment-")){
					TaskAttachmentMapper taskMapper = TaskAttachmentMapper.createFrom(entry.getValue());
					Date date = taskMapper.getCreationDate();
					String description = taskMapper.getDescription();
					
					if(date != null && description != null && date.after(START_DATE) && description.
							equals("mylyn/context/zip")) {
						InputStream is = attachmentHandler.getContent(
								repository, new TaskTask(repository
										.getConnectorKind(), repository
										.getUrl(), bugId.toString()), entry
										.getValue(), new NullProgressMonitor());
				         ZipInputStream zis = new ZipInputStream(is);
				         zis.getNextEntry();
				         
				         
				         long duration = getDuration(zis);
			        	 logger.info("Find duration: " + duration);
			        	 
			             zis.close();
				         is.close();	
					}
				}
			}
		}
	}

	private static long getDuration(ZipInputStream zis) {
		Date startTime =  new Date(Long.MAX_VALUE);
		Date endTime = new Date(Long.MIN_VALUE);
		for(InteractionEvent event : eventsIn(zis)) {
			Date sd = event.getDate();
			Date ed = event.getEndDate();
			if(sd.before(startTime))
				startTime = sd;
			if(ed.after(endTime))
				endTime = ed;
		}
		return endTime.getTime() - startTime.getTime();
	}
	
	
	
	
	static void printElementsIn(final TaskRepository repository,
			BugzillaRepositoryConnector connector,
			BugzillaClient client,
			Set<Integer> bugIds) throws CoreException,
			MalformedURLException, IOException {		
		
		Map<Integer, Float> totalBugDOK = new HashMap<Integer, Float>();
		
		for(Integer bugId : bugIds){
			
			logger.info("Inspecting bug " + bugId);
			
			totalBugDOK.put(bugId, 0.0f);
			
			TaskData taskData = connector.getTaskData(repository, bugId.toString(), null);
			
			BugzillaTaskAttachmentHandler attachmentHandler = (BugzillaTaskAttachmentHandler) connector
					.getTaskAttachmentHandler();
		
			for(Map.Entry<String, TaskAttribute> a : taskData.getRoot().getAttributes().entrySet()){
				
				if(a.getKey().startsWith("task.common.attachment-")){
					
					TaskAttachmentMapper taskMapper = TaskAttachmentMapper
							.createFrom(a.getValue());
					Date date = taskMapper.getCreationDate();
					IRepositoryPerson auth = taskMapper.getAuthor();
					String author = auth==null ? "unknown_author" : auth.getPersonId();
					String description = taskMapper.getDescription();
					
					if(date != null && description != null && 
						date.after(START_DATE) && description.equals("mylyn/context/zip")){
						
						InputStream is = attachmentHandler.getContent(
								repository, new TaskTask(repository
										.getConnectorKind(), repository
										.getUrl(), bugId.toString()), a
										.getValue(), new NullProgressMonitor());
						
				         ZipInputStream zis = new ZipInputStream(is);				         
				         
				         zis.getNextEntry();
				         
			        	 for(InteractionEvent event : eventsIn(zis)){
			        		 Float dok = knowledgeMap.get(event.getStructureHandle());
			        		 if(dok==null)
			        			 dok = 0f;
			        		 logger.info(bugId + "\t" +author+"\t"+date + "\t" + dok +"\t"+
			        				 event.getInterestContribution() + "\t" + "\t" + event.getStructureHandle());
			        		 
			        		 if(dok>0)//only add positive DOKs
			        			 totalBugDOK.put(bugId, totalBugDOK.get(bugId) + dok);
			        	 }
	
				         zis.close();
				         is.close();				         
					}
				}
			}
		}
		
		for(Map.Entry<Integer, Float> entry : totalBugDOK.entrySet()) {
			logger.info("bugId="+entry.getKey()+"\t"+"sum_dok="+entry.getValue());
		}
	}
	
	private static List<InteractionEvent> eventsIn(InputStream zis) {
		try {
			SaxContextContentHandler contentHandler = new SaxContextContentHandler(null,null);
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(contentHandler);
			reader.parse(new InputSource(zis));
			InteractionContext context = contentHandler.getContext();
			return context.getInteractionHistory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new LinkedList<InteractionEvent>();
	}
	
}
