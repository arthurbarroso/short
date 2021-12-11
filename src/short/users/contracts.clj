(ns short.users.contracts)

(def UserData
  [:map
   [:email [:re #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$"]]
   [:password string?]
   [:password-confirmation string?]])

(def UserOut
  [:map
   [:user/email string?]
   [:user/uuid uuid?]
   [:user/created_at inst?]])
