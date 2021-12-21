(ns short.ui.text
  (:require [garden.core :refer [css]]))

(def text-sizes
  {:xs "0.694em"
   :sm "0.833em"
   :md "1.200em"
   :lg "1.444em"
   :xl "1.728em"
   :xxl "2.073em"
   :xxxl "2.488em"})

(def text-spaces
  {:sm "12px"
   :md "24px"
   :lg "48px"
   :xl "72px"})

(def text-css
  (reduce #(str %1 "\n" %2)
          [(css [:h1 :.text-xxl {:font-size (:xxl text-sizes)
                                 :margin-bottom (:lg text-spaces)}])
           (css [:h2 :.text-xl {:font-size (:xl text-sizes)
                                :margin-bottom (:md text-spaces)}])
           (css [:h3 :.text-lg {:font-size (:lg text-sizes)}])
           (css [:h4 :.text-md {:font-size (:md text-sizes)}])
           (css [:small :.text-sm {:font-size (:sm text-sizes)}])
           (css [:.text-xs {:font-size (:xs text-sizes)}])
           (css [:p {:margin-bttom (:sm text-spaces)}])
           (css [:.text-xxxl {:font-size (:xxxl text-sizes)}])]))

(defn typography [{:keys [variant text sizing]}]
  [(keyword variant)
   {:class sizing}
   text])
