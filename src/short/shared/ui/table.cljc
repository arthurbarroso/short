(ns short.shared.ui.table
  (:require [garden.core :refer [css]]
            [short.shared.ui.button :as button]))

(def table-style
  {:width "100%"
   :text-align "left"
   :padding "2%"})

(def table-css
  (css [:.table table-style
        [:th {:font-weight "normal"
              :color "#080A0B"}]
        [:td {:color "#2B434A"}]
        [:button {:height "36px"
                  :width "36px"
                  :background "#66686A"}]]))

(defn get-item-status [item item-key]
  (if (get item (:key item-key))
    [:i {:class "fas fa-check-square"}]
    [:i {:class "far fa-check-square"}]))

(defn get-item [item-key item]
  (cond
    (:fun item-key) ((:fun item-key) (get item (:key item-key)))
    (:button item-key) [button/button
                        {:on-click #((-> item-key :button :function) item)
                         :text (-> item-key :button :text)}]
    (:checkbox item-key) (get-item-status item item-key)
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
