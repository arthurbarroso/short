(ns short.admin.css
  (:require [short.admin.template :as template]
            [short.ui.css :refer [ui-styles]]))

(defn clear-styles!
  "Remove existing style elements from the document <head>"
  []
  (let [styles (.getElementsByTagName js/document "style")
        style-count (.-length styles)]
    (doseq [n (range style-count 0 -1)] ;; deleting backward avoids any funny-business because the HTMLCollection in `styles` is a live list and changes under our feet
      (.remove (aget styles (dec n))))))

(defn mount-style
  "Mount the style-element into the header with `style-text`, ensuring this is the only `<style>` in the doc"
  [style-text]
  (let [head (or (.-head js/document)
                 (aget (.getElementsByTagName js/document "head") 0))
        style-el (doto (.createElement js/document "style")
                   (-> .-type (set! "text/css"))
                   (.appendChild (.createTextNode js/document style-text)))]
    #_(clear-styles!)
    (.appendChild head style-el)))

(defn mount-ui-styles []
  (let [template-styles template/layout-styles
        styles (str template-styles
                    "\n" ui-styles)]
    (mount-style styles)))
