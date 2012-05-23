(ns main.swt-simple-text-editor
  (:import [org.eclipse.swt SWT]
           [org.eclipse.swt.events SelectionAdapter SelectionEvent]
           [org.eclipse.swt.graphics Image]

           [org.eclipse.swt.widgets
            Button
            Dialog
            Display
            FileDialog
            Label
            Menu
            MenuItem
            MessageBox
            Shell
            Text
            ToolBar
            ToolItem]

           [java.io
            BufferedReader
            File
            FileNotFoundException
            FileReader
            FileWriter
            IOException])
  (:gen-class))

(defn create-about-dialog [parent]
  nil)

(defn gui-loop [display shell]
  "Main loop for SWT application"
  (when-not (.isDisposed shell)
    (if-not (.readAndDispatch display)
      (.sleep display))
    (recur display shell)))

(defn gui-main []
  (let [d (Display.)
        s (doto (Shell. d)
            (.setSize 500 500)
            (.setText "SWT Text Editor"))

        bar (ToolBar. s (. SWT HORIZONTAL))
        t (Text. s (bit-or (. SWT MULTI) (. SWT V_SCROLL) (. SWT H_SCROLL)
                           (. SWT WRAP)  (. SWT BORDER)))

        save-icon   (Image. d "images/save.jpg")
        open-icon   (Image. d "images/open.jpg")
        child-icon  (Image. d "images/userH.png")
        cut-icon    (Image. d "images/cut.jpg")
        copy-icon   (Image. d "images/copy.jpg")
        paste-icon  (Image. d "images/paste.jpg")

        open-tool-item   (ToolItem. bar (. SWT PUSH))
        save-tool-item   (ToolItem. bar (. SWT PUSH))
        sep1             (ToolItem. bar (. SWT SEPARATOR))
        cut-tool-item    (ToolItem. bar (. SWT PUSH))
        copy-tool-item   (ToolItem. bar (. SWT PUSH))
        paste-tool-item  (ToolItem. bar (. SWT PUSH))

        m                (Menu. s (. SWT BAR))
        file             (MenuItem. m (. SWT CASCADE))
        filemenu         (Menu. s (. SWT DROP_DOWN))
        open-menu-item   (MenuItem. filemenu (. SWT PUSH))
        save-menu-item   (MenuItem. filemenu (. SWT PUSH))
        separator        (MenuItem. filemenu (. SWT SEPARATOR))
        exit-menu-item   (MenuItem. filemenu (. SWT PUSH))
        edit             (MenuItem. m (. SWT CASCADE))
        edit-menu        (Menu. s (. SWT DROP_DOWN))
        copy-menu-item   (MenuItem. edit-menu (. SWT PUSH))
        cut-menu-item    (MenuItem. edit-menu (. SWT PUSH))
        paste-menu-item  (MenuItem. edit-menu (. SWT PUSH))
        help             (MenuItem. m (. SWT CASCADE))
        helpmenu         (Menu. s (. SWT DROP_DOWN))
        about-menu-item  (MenuItem. helpmenu (. SWT PUSH))

        open-file-action
        (proxy [SelectionAdapter] []
          (widgetSelected [evt]
            (let [file-dialog (doto (FileDialog. s (. SWT OPEN))
                                (.setText "Open")
                                (.setFilterPath "/home/cmpitg/tmp/")
                                (.setFilterExtensions
                                 (into-array String ["*.txt" "*.*"])))
                  selected (.open file-dialog)]
              (println ">> File selected:" selected)

              (if-not (nil? selected)
                (try
                  (.setText t (slurp (str selected)))

                  (catch FileNotFoundException e
                    (doto (MessageBox. s (bit-or (. SWT ICON_ERROR)
                                                 (. SWT OK)))
                      (.setMessage "Could not open file.")
                      (.setText "Error")
                      (.open)))

                  (catch IOException e
                    (doto (MessageBox. s (bit-or (. SWT ICON_ERROR)
                                                 (. SWT OK)))
                      (.setMessage "I/O Error.")
                      (.setText "Error")
                      (.open))))))))

        save-file-action
        (proxy [SelectionAdapter] []
          (widgetSelected [evt]
            (let [file-dialog (doto (FileDialog. s (. SWT SAVE))
                                (.setText "Save")
                                (.setFilterPath "/home/cmpitg/tmp/")
                                (.setFilterExtensions
                                 (into-array String ["*.txt" "*.*"])))
                  selected (.open file-dialog)]
              (when-not (nil? selected)
                (println ">> File selected:" selected)
                (try
                  (spit selected (.getText t))

                  (catch IOException e
                    (doto (MessageBox. s (bit-or (. SWT ICON_ERROR)
                                                 (. SWT OK)))
                      (.setMessage "File I/O Error.")
                      (.setText "Error")
                      (.open))))))))

        cut-action (proxy [SelectionAdapter] []
                     (widgetSelected [evt]
                       (.cut t)))

        copy-action (proxy [SelectionAdapter] []
                      (widgetSelected [evt]
                        (.copy t)))

        paste-action (proxy [SelectionAdapter] []
                       (widgetSelected [evt]
                         (.paste t)))]
    ;; UI widgets
    (doto bar
      (.setSize 500 55)
      (.setLocation 10 0))
    (.setBounds t 0 56 490 395)

    ;; Toolbar
    (doto open-tool-item
      (.setImage open-icon)
      (.setText "Open")
      (.setToolTipText "Open File"))
    (doto save-tool-item
      (.setImage save-icon)
      (.setText "Save")
      (.setToolTipText "Save File"))
    (doto cut-tool-item
      (.setImage cut-icon)
      (.setText "Cut")
      (.setToolTipText "Cut"))
    (doto copy-tool-item
      (.setImage copy-icon)
      (.setText "Copy")
      (.setToolTipText "Copy"))
    (doto paste-tool-item
      (.setImage paste-icon)
      (.setText "Paste")
      (.setToolTipText "Paste"))

    ;; Configure menu items
    (doto file
      (.setText "&File")
      (.setMenu filemenu))
    (doto open-menu-item
      (.setText "&Open\tCTRL+O")
      (.setAccelerator (+ (. SWT CTRL) (int \0))))
    (doto save-menu-item
      (.setText "&Save\tCTRL+S")
      (.setAccelerator (+ (. SWT CTRL) (int \S))))
    (.setText exit-menu-item "E&xit")
    (doto edit
      (.setText "&Edit")
      (.setMenu edit-menu))
    (.setText cut-menu-item "&Cut")
    (.setText copy-menu-item "Co&py")
    (.setText paste-menu-item "&Paste")
    (doto help
      (.setText "&Help")
      (.setMenu helpmenu))
    (.setText about-menu-item "&About")

    ;; Add events
    (.addSelectionListener open-tool-item open-file-action)
    (.addSelectionListener save-tool-item save-file-action)
    (.addSelectionListener cut-tool-item cut-action)
    (.addSelectionListener copy-tool-item copy-action)
    (.addSelectionListener paste-tool-item paste-action)
    (.addSelectionListener open-menu-item open-file-action)
    (.addSelectionListener save-menu-item save-file-action)
    (.addSelectionListener exit-menu-item
                           (proxy [SelectionAdapter] []
                             (widgetSelected [evt]
                               (.dispose d)
                               (System/exit 0))))
    (.addSelectionListener cut-menu-item cut-action)
    (.addSelectionListener copy-menu-item copy-action)
    (.addSelectionListener paste-menu-item paste-action)
    (.addSelectionListener about-menu-item
                           (proxy [SelectionAdapter] []
                             (widgetSelected [evt]
                               (create-about-dialog s))))

    (doto s
      (.setMenuBar m)
      (.pack)
      (.open))
    (gui-loop d s)
    (.dispose d)))

(defn -main [& args]
  (gui-main))