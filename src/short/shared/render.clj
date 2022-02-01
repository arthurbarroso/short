(ns short.shared.render
  (:require [clojure.string :as string]))

(defn add-component-string-to-html [html component-string]
  (string/replace html "${{html-string}}" component-string))

(defn add-scripts-to-include-to-html [html scripts-to-include]
  (string/replace html "${{scripts-to-include}}" scripts-to-include))

(defn add-hydrate-script-fn-to-html [html hydrate-script-fn]
  (string/replace html "${{hydrate-script}}" hydrate-script-fn))

(defn add-title-to-html [html title]
  (string/replace html "${{title}}" title))

(defn add-stylesheets-to-html [html stylesheets]
  (string/replace html "${{stylesheets}}" stylesheets))

(defn html-template
  [{:keys [pre-rendered-html
           hydrate-script-fn
           scripts-to-include
           title
           stylesheets]}]
  (->
   "<html>
      <head>
        <meta charset=\"utf-8\">
        ${{stylesheets}}
        ${{scripts-to-include}}
        <title>${{title}}</title>
      </head>
      <body>
        <div id=\"root\">${{html-string}}</div>
      </body>
      <script>${{hydrate-script}}</script>
    </html>"
   (add-component-string-to-html pre-rendered-html)
   (add-hydrate-script-fn-to-html hydrate-script-fn)
   (add-scripts-to-include-to-html scripts-to-include)
   (add-title-to-html title)
   (add-stylesheets-to-html stylesheets)))
