package com.example.swtapp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WebView2DownloadApp {
    private Display display;
    private Shell shell;
    private Browser browser;
    private Text textInput;
    private Button downloadBtn;
    private Label statusLabel;

    public static void main(String[] args) {
        new WebView2DownloadApp().run();
    }

    public void run() {
        display = new Display();
        createShell();
        createControls();
        
        shell.open();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
        display.dispose();
    }

    private void createShell() {
        shell = new Shell(display);
        shell.setText("WebView2 Download Application");
        shell.setSize(1000, 700);
        shell.setLayout(new GridLayout(1, false));
    }

    private void createControls() {
        // Create input section
        createInputSection();
        
        // Create browser
        createBrowser();
        
        // Create status section
        createStatusSection();
    }

    private void createInputSection() {
        Composite inputComposite = new Composite(shell, SWT.NONE);
        inputComposite.setLayout(new GridLayout(3, false));
        inputComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        Label inputLabel = new Label(inputComposite, SWT.NONE);
        inputLabel.setText("Enter text to download:");

        textInput = new Text(inputComposite, SWT.BORDER);
        textInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textInput.setText("Hello, this is sample text for download!");

        downloadBtn = new Button(inputComposite, SWT.PUSH);
        downloadBtn.setText("Download as .txt");
        downloadBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                downloadText();
            }
        });
    }

    private void createBrowser() {
        try {
            // Create WebView2 browser (Edge-based)
            browser = new Browser(shell, SWT.EDGE);
            browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            
            // Load a simple HTML page with download functionality
            String html = createDownloadHTML();
            browser.setText(html);
            
        } catch (Exception e) {
            // Fallback to default browser if WebView2 is not available
            browser = new Browser(shell, SWT.NONE);
            browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            browser.setText("<h2>WebView2 not available, using default browser</h2>");
            updateStatus("Warning: WebView2 not available, using fallback browser");
        }
    }

    private void createStatusSection() {
        statusLabel = new Label(shell, SWT.NONE);
        statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        statusLabel.setText("Ready");
    }

    private String createDownloadHTML() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Download Demo</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .container { max-width: 800px; margin: 0 auto; }
                    .download-section { 
                        background: #f5f5f5; 
                        padding: 20px; 
                        border-radius: 8px; 
                        margin: 20px 0; 
                    }
                    .csv-section {
                        background: #e8f5e8;
                        padding: 20px;
                        border-radius: 8px;
                        margin: 20px 0;
                        border-left: 4px solid #28a745;
                    }
                    input[type="text"] { 
                        width: 300px; 
                        padding: 8px; 
                        margin: 10px; 
                        border: 1px solid #ccc; 
                        border-radius: 4px; 
                    }
                    button { 
                        padding: 10px 20px; 
                        background: #007acc; 
                        color: white; 
                        border: none; 
                        border-radius: 4px; 
                        cursor: pointer; 
                        margin: 10px; 
                    }
                    button:hover { background: #005fa3; }
                    .csv-button {
                        background: #28a745;
                    }
                    .csv-button:hover {
                        background: #218838;
                    }
                    .console-output {
                        background: #2d3748;
                        color: #e2e8f0;
                        padding: 15px;
                        border-radius: 6px;
                        font-family: 'Courier New', monospace;
                        font-size: 12px;
                        max-height: 200px;
                        overflow-y: auto;
                        margin-top: 10px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>WebView2 Download Demo</h1>
                    <p>This page demonstrates download functionality within the WebView2 browser.</p>
                    
                    <div class="csv-section">
                        <h3>CSV Test Download</h3>
                        <p>Click the button below to trigger the testDownload() function:</p>
                        <button class="csv-button" onclick="testDownload()">Test CSV Download</button>
                        <div id="console" class="console-output">Console output will appear here...</div>
                    </div>
                    
                    <div class="download-section">
                        <h3>Web-based Download</h3>
                        <input type="text" id="webText" placeholder="Enter text to download..." 
                               value="This is web-generated content!">
                        <button onclick="downloadFromWeb()">Download from Web</button>
                    </div>
                    
                    <div class="download-section">
                        <h3>Sample Files</h3>
                        <button onclick="downloadSample('sample.txt', 'This is a sample text file generated from the web!')">
                            Download Sample Text
                        </button>
                        <button onclick="downloadSample('info.txt', 'Application: WebView2 Download Demo\\nDate: ' + new Date().toLocaleString())">
                            Download Info File
                        </button>
                    </div>
                </div>

                <script>
                    // Console logging helper
                    function logToConsole(message) {
                        const consoleDiv = document.getElementById('console');
                        const timestamp = new Date().toLocaleTimeString();
                        consoleDiv.innerHTML += `[${timestamp}] ${message}<br>`;
                        consoleDiv.scrollTop = consoleDiv.scrollHeight;
                    }

                    // Override console.log to show in our custom console
                    const originalConsoleLog = console.log;
                    const originalConsoleError = console.error;
                    
                    console.log = function(...args) {
                        originalConsoleLog.apply(console, args);
                        logToConsole('LOG: ' + args.join(' '));
                    };
                    
                    console.error = function(...args) {
                        originalConsoleError.apply(console, args);
                        logToConsole('ERROR: ' + args.join(' '));
                    };

                    // Your testDownload function
                    async function testDownload(){
                        try{
                            const data = "Column1,Column2,Column3\\nValue1,Value2,Value3";
                            const blob = new Blob([data], {type:'text/csv'});
                            console.log("Blob created: " + blob.size + " bytes, " + blob.type);
                            
                            const headers = new Headers();
                            headers.append('Content-Type','text/csv');
                            headers.append('Content-Disposition','attachment; filename="test.csv"');
                            headers.append('Content-Length',blob.size);
                            
                            console.log("Simulated Headers:");
                            for(const [key, value] of headers.entries()){
                                console.log(`${key}: ${value}`);
                            }
                            
                            try{
                                const url = window.URL.createObjectURL(blob);
                                const a = document.createElement('a');
                                a.href = url;
                                a.download = 'test.csv';
                                document.body.appendChild(a);
                                a.click();
                                document.body.removeChild(a);
                                window.URL.revokeObjectURL(url);
                                console.log("Native download triggered successfully");
                            }
                            catch(error){
                                console.error("Native download failed: " + error.message);
                            }
                        }
                        catch(error){
                            console.error("Test failed: " + error.message);
                        }
                    }

                    function downloadFromWeb() {
                        const text = document.getElementById('webText').value;
                        downloadSample('web-download.txt', text);
                    }
                    
                    function downloadSample(filename, content) {
                        const blob = new Blob([content], { type: 'text/plain' });
                        const url = window.URL.createObjectURL(blob);
                        const a = document.createElement('a');
                        a.href = url;
                        a.download = filename;
                        document.body.appendChild(a);
                        a.click();
                        window.URL.revokeObjectURL(url);
                        document.body.removeChild(a);
                    }

                    // Initialize console
                    logToConsole("Console initialized - Ready for downloads!");
                </script>
            </body>
            </html>
            """;
    }

    private void downloadText() {
        String content = textInput.getText().trim();
        
        if (content.isEmpty()) {
            updateStatus("Error: Please enter some text to download");
            return;
        }

        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setFilterNames(new String[]{"Text Files", "All Files"});
        dialog.setFilterExtensions(new String[]{"*.txt", "*.*"});
        dialog.setFileName("download_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt");

        String filePath = dialog.open();
        
        if (filePath != null) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(content);
                updateStatus("File downloaded successfully: " + filePath);
            } catch (IOException e) {
                updateStatus("Error downloading file: " + e.getMessage());
            }
        }
    }

    private void updateStatus(String message) {
        if (statusLabel != null && !statusLabel.isDisposed()) {
            statusLabel.setText(message);
        }
    }
}