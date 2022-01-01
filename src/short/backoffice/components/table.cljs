(ns short.backoffice.components.table
  (:require [reagent-table.core :as rt]))

(defn- row-key-fn
  [keyword row row-num]
  (str (get-in row keyword) row-num))

(defn- cell-data
  [row cell]
  (let [{:keys [path expr]} cell]
    (or (and path
             (get-in row path))
        (and expr
             (expr row)))))

(defn- cell-fn
  [render-info row _row-num _col-num]
  (let [{:keys [format attrs]
         :or   {format identity
                attrs (fn [_] {})}} render-info
        data    (cell-data row render-info)
        content (format data)
        attrs   (attrs data)]
    [:span
     attrs
     content]))

(def table-state (atom {:draggable true}))

(defn table [{:keys [columns items key]}]
  [rt/reagent-table items
   {:table-state table-state
    :row-key #(row-key-fn key %1 %2)
    :column-model columns
    :render-cell cell-fn
    :scroll-height "40vh"}])
