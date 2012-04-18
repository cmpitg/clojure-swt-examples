(ns main.swt-styledtext-indent-alignment-justify
  (:import [org.eclipse.swt.widgets Display Shell]
           [org.eclipse.swt.layout FillLayout]
           [org.eclipse.swt.custom StyledText]
           [org.eclipse.swt SWT]))

(defn gui-loop [display shell]
  "Main loop for SWT application"
  (when-not (.isDisposed shell)
    (if-not (.readAndDispatch display)
      (.sleep display))
    (recur display shell)))

(defn gui-main []
  (let [text (str "The first paragraph has an indentation of fifty pixels. Indentation is the amount of white space in front of the first line of a paragraph. If this paragraph wraps to several lines you should see the indentation only on the first line.\n\n"
                  "The second paragraph is center aligned. Alignment only works when the StyledText is using word wrap. Alignment, as with all other line attributes, can be set for the whole widget or just for a set of lines.\n\n"
                  "The third paragraph is justified. Like alignment, justify only works when the StyledText is using word wrap. If the paragraph wraps to several lines, the justification is performed on all lines but the last one.\n\n"
                  "The last paragraph is justified and right aligned. In this case, the alignment is only noticeable in the final line.")

        display (new Display)
        shell   (doto (new Shell display)
                  (.open)
                  (.setText "StyledText Demo")
                  (.setSize 300 400)
                  (.setLayout (FillLayout.)))

        _ (doto (StyledText. shell (bit-or (. SWT WRAP)
                                           (. SWT BORDER)))
            (.setText text)
            (.setLineIndent 0 1 50)
            (.setLineAlignment 2 1 (. SWT CENTER))
            (.setLineJustify 4 1 true)
            (.setLineAlignment 6 1 (. SWT RIGHT))
            (.setLineJustify 6 1 true))]

    (doto shell
      (.pack)
      (.open))

    (gui-loop display shell)
    (.dispose display)))

(defn -main [& args]
  (println "Hello")
  (gui-main))
