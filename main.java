package pcc.ui;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pcc.analysis.ChangeCounterUtils;
import pcc.io.IOUtils;
import pcc.vercon.Project;
import pcc.vercon.ProjectVersion;


public class Main{
  public static Project project;
	public static String projectName;
	public static PCCFrame frame;
	private static Scanner in;
	public static void main(String[] args){
		//init resources
		in = new Scanner(System.in);
		frame = new PCCFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void openProject(){
		//System.out.print("Project name: ");
		//projectName = in.nextLine();
		projectName = JOptionPane.showInputDialog("Please enter project name: ");
		try {
			project = IOUtils.openProject(projectName+File.separator+"project.dat");
		} catch (Throwable t){
			JOptionPane.showMessageDialog(null,"Error opening project file.");
		}
	}
	private static void saveProject(){
		if(project==null)
			return;
		try {
			File dir = new File(projectName);
			if(!dir.exists())
				IOUtils.createFolder(projectName);
			IOUtils.saveProject(project, projectName+File.separator+"project.dat");
		} catch (Throwable t) {
			t.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error updating project file.");
		}
	}
	public static void explicitSave(){
		if(project==null){
			//System.out.println("Please open or create a project first.");
			JOptionPane.showMessageDialog(null,"Please open or create a project first.");
			return;
		}
		saveProject();
	}
	public static void newProject(){
		//System.out.print("Project name: ");
		//projectName = in.nextLine();
		projectName = JOptionPane.showInputDialog("Please enter project name: ");
		
		if(!projectName.matches("[a-zA-Z0-9_]+"))
			//System.out.println("Invalid name entered: letters and underscores only.");
			JOptionPane.showMessageDialog(null,"Invalid name entered: letters and underscores only.");
		else{
			if((new File(projectName)).exists())
				//System.out.println("A folder/file with the given name already exists.");
				JOptionPane.showMessageDialog(null,"A folder/file with the given name already exists.");
			else{
				project = new Project();
				saveProject();
			}
		}
	}
	public static void addFile(){
		if(project==null){
			//System.out.println("Please open or create a project first.");
			JOptionPane.showMessageDialog(null,"Please open or create a project first.");
			return;
		}
		//System.out.print("File name: ");
		//String name = in.nextLine();
		String name = JOptionPane.showInputDialog("Please enter File name: ");
		try{
			File file = new File(name);
			if(!file.exists())
				throw new RuntimeException();
		}
		catch(Throwable t){
			//System.out.println("Error adding file.");
			JOptionPane.showMessageDialog(null,"Error adding file.");
			return;
		}
		for(String fn:project.getFiles())
			if(fn.equalsIgnoreCase(name)){
				//System.out.println("That file is already being monitored: see \"files\"");
				JOptionPane.showMessageDialog(null,"That file is already being monitored: see \"files\"");
				
				return;
			}
		project.addFile(name);
		saveProject();
	}
	public static void removeFile(String name){
		if(!project.removeFile(name))
			JOptionPane.showMessageDialog(frame, "File Not Found!");
		saveProject();
	}
	public static void commitNewVersion(){
		if(project==null){
			//System.out.println("Please open or create a project first.");
			JOptionPane.showMessageDialog(null,"Please open or create a project first.");
			return;
		}
		//System.out.print("Version number: ");
		//String number = in.nextLine();
		String number = JOptionPane.showInputDialog("Please enter a version number");
		if(project.getVersion(number)!=null){
			//System.out.println("That version already exists: see \"versions\"");
			JOptionPane.showMessageDialog(null,"That version already exists: see \"versions\"");
			return;
		}
		//System.out.print("Author name: ");
		//String author = in.nextLine();
		//System.out.print("Reason for commit: ");
		//String reason = in.nextLine();
		String author = JOptionPane.showInputDialog("Author name");
		String reason = JOptionPane.showInputDialog("Reason for commit");
		try {
			project.commit(number, author, reason);
		} catch (IOException e) {
			//System.out.println("Error commiting source files.");
			JOptionPane.showMessageDialog(null,"Error commiting source files.");
		}
		saveProject();
	}
	public static void exportChangeLables(){
		if(project==null){
			//System.out.println("Please open or create a project first.");
			JOptionPane.showMessageDialog(null,"Please open or create a project first.");
			return;
		}
		//System.out.print("First version: ");
		//String version1 = in.nextLine();
		String version1 = JOptionPane.showInputDialog("First version:  ");
		//System.out.print("Last version: ");
		//String version2 = in.nextLine();
		String version2 = JOptionPane.showInputDialog("Last version:  ");
		//System.out.print("Output directory: ");
		//String dir = in.nextLine();
		String dir = JOptionPane.showInputDialog("Output directory:  ");
		try {
			if(IOUtils.fileExists(dir)){
				//System.out.println("Are you sure you want to delete all files in this directory? (N/y)");
				//String confirm = in.nextLine();
				String confirm = JOptionPane.showInputDialog("Are you sure you want to delete all files in this directory? (N/y");
				if(!confirm.matches("([yY])|([yY][eE][sS])"))
					return;
				IOUtils.deleteFolder(dir);
			}
			ChangeCounterUtils.exportChangeLabels(dir,
					project.getVersion(version1), project.getVersion(version2));
		} catch (IOException e) {
			e.printStackTrace();
			//System.out.println("Error exporting change labels.");
			JOptionPane.showMessageDialog(null,"Error exporting change labels.");
		}
	}
	public static void displayAllFiles(){
		if(project==null){
			//System.out.println("Please open or create a project first.");
			JOptionPane.showMessageDialog(null,"Please open or create a project first.");
			return;
		}
		for(String fn:project.getFiles())
			System.out.println(fn);
	}
	public static void displayCurrentVersion(){
		if(project==null){
			//System.out.println("Please open or create a project first.");
			JOptionPane.showMessageDialog(null,"Please open or create a project first.");
			return;
		}
		System.out.println(project.getCurrentVersion());
	}
	public static void displayAllVersions(){
		if(project==null){
			//System.out.println("Please open or create a project first.");
			JOptionPane.showMessageDialog(null,"Please open or create a project first.");
			
			return;
		}
		for(String v:project.getVersionList())
			System.out.println(v);
	}
	public static String displayVersionData(String number){
		if(project==null){
			//System.out.println("Please open or create a project first.");
			JOptionPane.showMessageDialog(null,"Please open or create a project first.");
			return "";
		}
		ProjectVersion version = project.getVersion(number);
		String ret = "";
		if(version==null)
			//System.out.println("Invalid version number: see \"versions\"");
		JOptionPane.showMessageDialog(null,"Invalid version number: see \"versions\"");
		else{
			ret+="<html>"+version.getMetaData();
			ret+="<br>Total LLOC: "+ChangeCounterUtils.getLLOC(version);
			ret+="</br></html>";
		}
		return ret;
	}
	public static void displayVersionChanges(boolean shortReport){
		if(project==null){
			//System.out.println("Please open or create a project first.");
			JOptionPane.showMessageDialog(null,"Please open or create a project first.");
			return;
		}
		//System.out.print("First version: ");
		//String version1 = in.nextLine();
		String version1 = JOptionPane.showInputDialog("First version");
		ProjectVersion v1 = project.getVersion(version1);
		if(v1==null){
			//System.out.println("Invalid version number.");
			JOptionPane.showMessageDialog(null,"Invalid version number.");
			return;
		}
		//System.out.print("Last version: ");
		//String version2 = in.nextLine();
		String version2 = JOptionPane.showInputDialog("Last version");
		ProjectVersion v2 = project.getVersion(version2);
		if(v2==null){
			//System.out.println("Invalid version number.");
			JOptionPane.showMessageDialog(null,"Invalid version number.");
			return;
		}
		
		String report = "";
		if(shortReport)
			report = ChangeCounterUtils.getLLOCChanges(v1, v2);
		else
			report = ChangeCounterUtils.getLineChangesReport(v1, v2);
		System.out.println(report);
	}
}
