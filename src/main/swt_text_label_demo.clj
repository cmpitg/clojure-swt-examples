(ns main.swt-text-label-demo
  (:import [org.eclipse.swt           SWT]
           [org.eclipse.swt.widgets   Display Shell Button Combo Label Text]
           [org.eclipse.swt.custom    CLabel]
           [org.eclipse.swt.graphics  Image]
           [org.eclipse.swt.layout    GridData GridLayout]))

(defn gui-loop [display shell]
  (when-not (. shell (isDisposed))
    (if-not (. display (readAndDispatch))
      (. display (sleep)))
    (recur display shell)))

(defn make-label
  ([text parent] (make-label text parent (. SWT NULL)))
  ([text parent style] (doto (Label. parent style)
                         (.setText text))))
(defn make-text
  ([text parent] (make-text text parent (bit-or (. SWT SINGLE)
                                                (. SWT BORDER))))
  ([text parent style] (doto (Text. parent style)
                         (.setText text))))

(defn make-grid-data-default []
  (GridData. (. GridData HORIZONTAL_ALIGN_FILL)))

(defn make-grid-data-default-2 []
  (let [g (make-grid-data-default)]
    (set! (. g horizontalSpan) 3)
    g))

(defn gui-main []
  (let [display (Display.)
        shell (doto (Shell. display)
                (.setText "Book Entry Demo")
                (.setLayout (let [g (GridLayout. 4 false)]
                              (set! (. g verticalSpacing) 8)
                              g)))

        ;; Title
        _ (make-label "Title: " shell)
        title (doto (make-text "Professional Java Interfaces with SWT/JFace"
                               shell)
                (.setLayoutData (make-grid-data-default-2)))

        ;; Authors
        _ (make-label "Author(s): " shell)
        authors (doto (make-text "Jack Li Guojie" shell)
                  (.setLayoutData (make-grid-data-default-2)))

        ;; Cover
        _ (doto (make-label "Cover: " shell)
            (.setLayoutData
             (let [g (GridData.)]
               (set! (. g verticalSpan) 3)
               g)))
        cover (doto (CLabel. shell (. SWT NULL))
                (.setLayoutData
                 (let [g (make-grid-data-default)]
                   (set! (. g horizontalSpan) 1)
                   (set! (. g verticalSpan) 3)
                   (set! (. g heightHint) 100)
                   (set! (. g widthHint) 100)
                   g)))

        ;; Details
        _ (make-label "Pages" shell)
        pages (doto (make-text "500pp" shell)
                (.setLayoutData (make-grid-data-default)))

        _ (make-label "Publisher" shell)
        publisher (doto (make-text "John Wiley & Sons" shell)
                    (.setLayoutData (make-grid-data-default)))

        _ (make-label "Rating" shell)
        rating (doto (Combo. shell (. SWT READ_ONLY))
                 (.setLayoutData (make-grid-data-default))
                 (.add "5")
                 (.add "4")
                 (.add "3")
                 (.add "2")
                 (.add "1"))

        ;; Abstract
        _ (make-label "Abstract:" shell)
        book-abstract (doto (make-text
                             (str "This book provides a comprehensive"
                                  "guide for \nyou to create Java user"
                                  "interfaces with SWT/JFace. ")
                             shell
                             (bit-or (. SWT WRAP)
                                     (. SWT MULTI)
                                     (. SWT BORDER)
                                     (. SWT H_SCROLL)
                                     (. SWT V_SCROLL)))
                        (.setLayoutData
                         (let [g (GridData. (bit-or
                                             (. GridData HORIZONTAL_ALIGN_FILL)
                                             (. GridData VERTICAL_ALIGN_FILL)))]
                           (set! (. g horizontalSpan) 1)
                           (set! (. g grabExcessVerticalSpace) true)
                           g)))

        ;; Button
        enter-button (doto (Button. shell (. SWT PUSH))
                       (.setText "Enter")
                       (.setLayoutData
                        (let [g (GridData.)]
                          (set! (. g horizontalSpan) 4)
                          (set! (. g horizontalAlignment) (. GridData END))
                          g)))
        ]

    (doto shell
      (.pack)
      (.open))

    (gui-loop display shell)
    (. display (dispose))))

(defn -main [& args]
  (gui-main))
