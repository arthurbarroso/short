(ns short.admin.views.login
  (:require [short.admin.components.template :as template]
            [short.admin.components.button :as button]
            [short.admin.components.input :as input]
            [stylefy.core :refer [use-style]]))

(def form-style
  {:display "flex"
   :flex-direction "column"
   :align-items "center"
   :max-width "450px"
   :width "100%"
   :padding "2%"
   :border-radius "8px"
   :box-shadow "0 0 5pt 2pt #ededed"})

(def section-style
  {:display "flex"
   :justify-content "center"
   :align-items "center"
   :height "100%"})

(def input-extra-styles
  {:width "100%"})

(def label-style
  {:align-self "flex-start"})

(defn login-view []
  [template/layout
   ^{:key "login"}
   [:section (use-style section-style)
    [:form (use-style form-style)
     [:h2 (use-style {:align-self "flex-start"})
      "Log in to the admin panel"]
     [:label (use-style label-style)
      "E-mail address"]
     [input/input {:type "email"
                   :placeholder "e-mail address"
                   :on-change #()
                   :extra-styles input-extra-styles}]
     [:label (use-style label-style)
      "Password"]
     [input/input {:type "password"
                   :placeholder "password"
                   :on-change #()
                   :extra-styles input-extra-styles}]

     [button/button {:text "Log in"
                     :extra-styles {:width "100%"
                                    :margin-top "3%"}}]]]])
