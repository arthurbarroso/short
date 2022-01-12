(ns util.gen-css
  (:require [short.ui.css :as css]))

(defn main [& _opts]
  (let [styles css/ui-styles]
    (do
      (spit "resources/assets/stylesheet.css" styles))))
