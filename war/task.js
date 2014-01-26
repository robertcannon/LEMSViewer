

// all we need do here is load the GWT js and create an instance of SimWorkerServer

// send a message to the GWT client to say we're ready.
// it should respond with the source path for the GWT js
self.postMessage("LOG Loaded new basic js");


// viewer.nocache assigns shortcuts to these but should never call methods on them in worker model


var window = {};
var document = {};
window.document = document;
var navigator = {};


navigator.userAgent = "webkit";

window.isDummy = true;

document.getElementsByTagName = function() {return []};
document.getElementById = function() { return null;};
document.location = {}; 
document.location.href = "";
var created = {};
document.createElement = function(typ){console.log("tried to create " + typ); return created;};
document.write = function(str){console.log("tried to write " + str); console.log(created);};

document.compatMode = "CSS1Compat";

importScripts("worker/worker.nocache.js?a=1234");

self.postMessage("LOG Loaded worker.nocache.js setting readyState complete");

document.readyState = "complete";

