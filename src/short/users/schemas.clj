(ns short.users.schemas)

(def user-db-schema
  [;;user/uuid
   {:db/ident :user/uuid
    :db/valueType :db.type/uuid
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one}
   ;;user/email
   {:db/ident :user/email
    :db/unique :db.unique/value
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   ;;user/active
   {:db/ident :user/active
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   ;;user/password
   {:db/ident :user/password
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   ;;user/created_at
   {:db/ident :user/created_at
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}])

(def User
  [:map
   [:user/email string?]
   [:user/password string?]
   [:user/uuid uuid?]
   [:user/created_at inst?]
   [:user/active boolean?]])

(def ExistingUser
  [:map
   [:user/email string?]
   [:user/password string?]
   [:user/uuid uuid?]
   [:user/created_at inst?]
   [:user/active boolean?]
   [:db/id int?]])

(def TokenOut
  [:maybe
   [:map [:token string?]]])

(def UserQueryResult
  [:or
   [:vector
    [:vector
     ExistingUser]]
   [:vector empty?]])

(def CredentialsCheck
  [:map
   [:matches? boolean?]
   [:existing-user [:or nil? ExistingUser]]])
