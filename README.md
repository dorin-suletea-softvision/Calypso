# Calypso
  * Cross-platform UML Modeling <br>
Java reflection based UML drawer. 
Motivation : in different contexts a developer need different kinds of diagrams.
One tool for several diagram types that multiple tools.


### The plugin implementator must define
  * Entities (classes/interfaces/actors) 
  * Possible actions between the Entities and restriction rules.
  * The look of each Entity/Relation (the actual shape of the entity)
  * All the plugins must adhere to a preset interface.
  * Export the plugin to plugin_repo.<br>

An example of a diagram drawn by the demo plugin can be found int dist\Calypso_Home\CLY_Workspace <br>
RR_obj_diagram.png

### To Run :
 * From project root/dist folder download Calypso_Home folder from the dist
 * Make sure jdk is installed on the machine and run Calypso_run_windows.bat form the downloaded folder. (for Linux you will have to adapt the script)
 * Load the plugin and create a new document or load one from CLY_Workspace.

### From eclipse:
 * In eclipse import existing project from sources.
 * The demo plugin is contained in the project
 * To modify the plugin checkout https://github.com/dorin-suletea/Calypso-Demo-Plugin (if you don't follow this steps the application will not load the modified plugin)
    * Import eclipse project from sources
    * Export jar (not runnable)
    * Move the jar to <calypso_project_dir>/ plugin_repo 
    * Open calypso and select your plugin.
 
 
 ### Note: The common classes between plugins and main application are not extracted to a separate jar (and contain the same code)
 If you decide to modify the communication interface the modifications must be made both in the plugin and the main app.
 The interfaces are not packaged in a jar because the communication interface design is still work in progress.
