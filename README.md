# rangedclassification
A range-based classification rule deriving programme used to analyse data guided by class values 

Implementation based on previous paper by Dr Shao, J. and his colleagues (link: http://dx.doi.org/10.1109/FSKD.2011.6019723)

Dataset (Iris, Wine) from UCI Datasets (link: https://archive.ics.uci.edu/ml/datasets.html)

Report from other repo (link: https://github.com/Phuongt994/rangedclassificationreport)

.idea and out folder for Intellij IDE project export.
.classpath and .project for Eclipse IDE project export

---

HOW TO RUN:
- Redirect the programme to appropriate dataset 

Open Scanners and replace dataDirectory = "yourdataset.csv" 

- Specify threshold for support confidence and density check

Open Analyser and in checkSupportConfidence(), replace support and confidence to your desired float value

Open SubAnalyser and in checkDensity(), replace density to your desired float value

- (Optional) Set output to console.txt file to save log.

- Run Starter to start main()

- Check output in classifier.txt once programme finishes
