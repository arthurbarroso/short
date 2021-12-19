(ns util.gen-css
  (:require [short.ui.css :as css]))

(defn main [& _opts]
  (let [styles css/ui-styles]
    (spit "public/assets/stylesheet.css" styles)))
