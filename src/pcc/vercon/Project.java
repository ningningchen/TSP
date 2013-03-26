package pcc.vercon;
import java.util.ArrayList;
import java.util.HashMap;


public class Project implements java.io.Serializable{
	private String latestVersion;
	/**
	 * version number -> project version file name
	 */
	private ArrayList<String> versions;
	private ArrayList<String> sourceFileNames;
	
	private ArrayList<ProjectVersion>	m_projVersions;
	
	public  Project()
	{
		versions		=	new ArrayList();
		sourceFileNames	=	new ArrayList();
		m_projVersions	=	new ArrayList();
		
	}
	
	public void commit(String number, String author, String reason)
	{
		ProjectVersion	projVersion		=	new ProjectVersion (number, author, reason, sourceFileNames);
		
		m_projVersions.add(projVersion);
		
		versions.add(number);
		latestVersion	=	number;

	}
	
	public void addFile(String filename)
	{
		sourceFileNames.add(filename);
	}
	
	public void removeFile(String filename)
	{
		sourceFileNames.remove(filename);
	}
	
	public ArrayList<String> getFiles()
	{
		return (sourceFileNames);
	}
	
	public String getCurrentVersion()
	{
		return latestVersion;
	}
	
	public ProjectVersion getVersion(String number)
	{
		ProjectVersion		retProjVersion	=	null;
		
		for (int ii = 0; ii < m_projVersions.size(); ii ++)
		{
			if (m_projVersions.get(ii).getNumber().equalsIgnoreCase(number))
			{
				retProjVersion	=	m_projVersions.get(ii);
				break;
			}
		}
			

		return retProjVersion;
	}

	public ArrayList<String> getVersionList()
	{
		return (versions);
	}
}
