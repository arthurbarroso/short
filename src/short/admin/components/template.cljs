(ns short.admin.components.template
  (:require [reagent.core :as r]
            [stylefy.core :refer [use-style]]))

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

(defn layout [_children]
  (r/create-class
   {:reagent-render
    (fn [children]
      [:div (use-style app-base-style)
       [:div (use-style content-style)
        [:<>] ;; header
        [:div (use-style container-style)
         (into [:<> children])]]])}))
