(ns short.ui.table
  (:require [garden.core :refer [css]]
            [short.ui.button :as button]))

(def table-style
  {:width "100%"
   :text-align "left"
   :border "1px solid #eee"
   :padding "2%"})

(def table-css
  (css [:.table table-style]))

(defn get-item [item-key item]
  (cond
    (:fun item-key) ((:fun item-key) (get item (:key item-key)))
    (:button item-key) [button/button-outlined
                        {:on-click #((-> item-key :button :function) item)
                         :text (-> item-key :button :text)}]
    :else (get item (:key item-key))))

(defn get-item-key [item-key item key]
  (cond
    (:button item-key) (str "button-" (get item key) (:button-key item-key))
    :else (str (get item key) item-key (get item (:key item-key)))))

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
         ^{:key (get-item-key item-key item key)}
         [:td
          (get-item item-key item)])])]])
