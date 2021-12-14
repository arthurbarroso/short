(ns short.admin.template
  (:require [reagent.core :as r]
            [garden.core :refer [css]]))

(def app-base-style
  {:height "100vh"
   :display "flex"})

(def content-style
  {:display "flex"
   :background "#FFF"
   :height "100vh"
   :flex-direction "column"
   :width "100%"
   :box-sizing "border-box"
   :-webkit-box-sizing "border-box"
   :-moz-box-sizing "border-box"})

(def container-style
  {:padding "0 5%"
   :height "100%"})

(def layout-styles
  (str (css [:.base app-base-style])
       "\n"
       (css [:.content content-style])
       "\n"
       (css [:.container container-style])
       "\n"))

(defn layout [_children]
  (r/create-class
   {:reagent-render
    (fn [children]
      [:div {:class "base"}
       [:div {:class "content"}
        [:<>] ;; header
        [:div {:class "container"}
         (into [:<> children])]]])}))
