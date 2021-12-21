(ns short.ui.template
  (:require [garden.core :refer [css]]
            [short.ui.text :as text]))

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

(def layout-css
  (str (css [:.base app-base-style])
       "\n"
       (css [:.content content-style])
       "\n"
       (css [:.container container-style])
       "\n"))

(defn template [children]
  [:div {:class "base"}
   [:div {:class "content"}
    [:div {:class "container"}
     [:header
      [:a {:href "/"}
       (text/typography {:text "short"
                         :variant "h1"
                         :sizing "text-xxxl"})]]
     children]]])
