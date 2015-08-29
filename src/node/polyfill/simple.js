// When using :simple optimization some polyfill needs to be loaded
// before modules that would fail without.
// Force by loading this as :foreign-libs in the project file, e.g.:
// This injects this file on top of the executable.

var path = require("path"); // not strictly needed
//global.goog = {provide : function() {},
//               require : require };

var React = require("react"); // maybe compat can set js/React instead?

// Stand-ins redefined elsewhere... for hickory.core.cljs
function Node() {};
Node.ELEMENT_NODE = 1;
Node.ATTRIBUTE_NODE = 2;
Node.TEXT_NODE = 3;
Node.CDATA_SECTION_NODE = 4;
Node.ENTITY_REFERENCE_NODE = 5;
Node.ENTITY_NODE = 6;
Node.PROCESSING_INSTRUCTION_NODE = 7;
Node.COMMENT_NODE = 8;
Node.DOCUMENT_NODE = 9;
Node.DOCUMENT_TYPE_NODE = 10;
Node.DOCUMENT_FRAGMENT_NODE = 11;
Node.NOTATION_NODE = 12;

function NodeList() {};
NodeList.prototype = {
    length:0,
    item: function (index) {
        return this[index] || null;
    }
}

// require(path.join(path.resolve("."),"server", "polyfill", "compat.js"));
// require("server/polyfill/compat.js");
