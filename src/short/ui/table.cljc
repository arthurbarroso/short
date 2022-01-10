(ns short.ui.table
  (:require [garden.core :refer [css]]))

(def table-style
  {:width "100%"
   :text-align "left"})

(def table-css
  (css [:.table table-style]))

(defn table
  [{:keys [columns items item-keys key]}]
  [:table {:class "table"}
   [:thead
    [:tr
     (for [column columns]
       ^{:key column}
       [:th column])]]
   [:tbody
    (for [item items]
      ^{:key (get item key)}
      [:tr
       (for [item-key item-keys]
         ^{:key (str (get item key) (get item (:key item-key)))}
         [:td
          (if (:fun item-key)
            ((:fun item-key (get item (:key item-key))))
            (get item (:key item-key)))])])]])
