(ns re-frame.core
  (:require [re-frame.scaffold :as scaffold]
            [re-frame.middleware :as middleware]))

; this file provides public API to default re-frame setup
; note: by including this namespace, you will get default app-db, default app-frame and default event channel
; plus it will start default router-loop automatically

; * app-db is backed by reagent/atom
; * app-frame has default loggers
; * router event channel is implemented using core.async channel

; the default instance of app-db
(def app-db (scaffold/make-app-db-atom))

; the default instance of re-frame
(def app-frame (scaffold/make-frame-atom))

; the default event queue
(def event-chan (scaffold/make-event-chan))

; --  API  ----------------------------------------------------------------------------------------------------------

(def set-loggers! (partial scaffold/set-loggers! app-frame))
(def register-sub (partial scaffold/register-sub app-frame))
(def unregister-sub (partial scaffold/unregister-sub app-frame))
(def clear-sub-handlers! (partial scaffold/clear-sub-handlers! app-frame))
(def subscribe (partial scaffold/subscribe app-frame app-db))
(def clear-event-handlers! (partial scaffold/clear-event-handlers! app-frame))
(def register-handler
  "register a handler for an event.
  This is low level and it is expected that \"re-frame.core/register-handler\" would
  generally be used."
  (partial scaffold/register-handler app-frame))
(def unregister-handler (partial scaffold/unregister-handler app-frame))

(def dispatch
  "Send an event to be processed by the registered handler.

Usage example:
   (dispatch [:delete-item 42])
"
  (partial scaffold/dispatch event-chan app-frame))

(def dispatch-sync
  "Send an event to be processed by the registered handler, but avoid the async-inducing
use of core.async/chan.

Usage example:
   (dispatch-sync [:delete-item 42])"
  (partial scaffold/dispatch-sync app-db app-frame))

; --  middleware  ---------------------------------------------------------------------------------------------------

(def debug (middleware/debug app-frame))
(def path (middleware/path app-frame))
(def enrich (middleware/enrich app-frame))
(def trim-v (middleware/trim-v app-frame))
(def after (middleware/after app-frame))
(def log-ex (middleware/log-ex app-frame))
(def on-changes (middleware/on-changes app-frame))

(def run-router-loop (partial scaffold/run-router-loop event-chan app-db app-frame))

; --  event processing  ---------------------------------------------------------------------------------------------

(run-router-loop)
