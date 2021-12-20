(ns util.gen-css
  (:require [short.ui.css :as css]))

(defn main [& _opts]
  (let [styles css/ui-styles]
    (do
      (spit "public/assets/stylesheet.css" styles)
      (spit "store/assets/stylesheet.css" styles))))
