(ns short.ui.template
  (:require [garden.core :refer [css]]
            [short.ui.text :as text]))

(def app-base-style
  {:height "100vh"
   :display "flex"})

(def content-style
  {:display "flex"
   :background "#bfd8ff"
   :height "100%"
   :flex-direction "column"
   :width "100%"
   :box-sizing "border-box"
   :-webkit-box-sizing "border-box"
   :-moz-box-sizing "border-box"
   :padding "2% 5%"})

(def container-style
  {:background "#FFF"
   :display "flex"
   :flex-direction "column"
   :box-sizing "border-box"
   :padding "1% 2%"
   :border-radius "8px"
   :overflow "auto"})

(def layout-css
  (str (css [:.base app-base-style])
       "\n"
       (css [:.content content-style])
       "\n"
       (css [:.container container-style])
       "\n"
       (css [:.header {:padding "0 2%"
                       :background "#FFF"
                       :margin-bottom "1%"
                       :border-radius "8px"}])
       "\n"
       (css [:.header-link {:text-decoration "none"
                            :color "#333"}])))

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
