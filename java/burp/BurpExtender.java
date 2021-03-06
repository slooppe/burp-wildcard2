package burp;

import com.snooze.burp.wildcard.BurpExtenderCallbacks;
import com.snooze.burp.wildcard.OptionsTab;
import com.snooze.burp.wildcard.Settings;

import java.io.PrintWriter;

public class BurpExtender implements IBurpExtender
{
    PrintWriter stdout;
    PrintWriter stderr;

    static BurpExtender burpExtender;
    OptionsTab options;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        burpExtender = this; BurpExtenderCallbacks.callbacks = callbacks;

        String tabname = BurpExtenderCallbacks.callbacks.loadExtensionSetting("com.snooze.burp.wildcard.name");
        if (tabname!=null) {
            Settings.tab_title = tabname;
        } else {
            BurpExtenderCallbacks.callbacks.saveExtensionSetting("com.snooze.burp.wildcard.name", Settings.tab_title);
        }

        // Set names
        callbacks.setExtensionName(Settings.extension_name);
//        Settings.tab_title =  BurpExtenderCallbacks.callbacks.loadExtensionSetting("com.snooze.burp.wildcard.name");
        
        // obtain our output and error streams
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);

        options = new OptionsTab();

        callbacks.addSuiteTab(options);

        BurpExtenderCallbacks.callbacks.registerExtensionStateListener(new IExtensionStateListener() {
            @Override
            public void extensionUnloaded() {
                BurpExtender.getInstance().stdout("Unregistering extension");
                options.showAll();
                //BurpExtenderCallbacks.callbacks.saveExtensionSetting("com.snooze.burp.wildcard.name", null);
                //BurpExtenderCallbacks.callbacks.saveExtensionSetting("com.snooze.burp.wildcard.hidden", null);
            }
        });
    }

    public void stdout(String s){
        stdout.println(s);
    }

    public void stderr(String s){
        stderr.println(s);
    }

    public void changeName(){
//        this.stdout("Changing name to: "+Settings.tab_title);

        BurpExtenderCallbacks.callbacks.saveExtensionSetting("com.snooze.burp.wildcard.name", Settings.tab_title);

        BurpExtenderCallbacks.callbacks.removeSuiteTab(options);
        BurpExtenderCallbacks.callbacks.addSuiteTab(options);
    }

    public static BurpExtender getInstance(){
        return burpExtender;
    }
}