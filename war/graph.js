


function log(str) {
	if (typeof(console) != "undefined" && console.log) {
		console.log(str);
	}
}


function docETAG(tag) {
	var ret = document.getElementsByTagName(tag);
	return ret;
}

function docEID(id) {
	var ret = document.getElementByID(id);
	return ret;
}


function getDisplayList(srcid) {
	var ret = null;
	if (window.graphs && window.graphs[srcid]) {
		ret = window.graphs[srcid].getDisplayList();
		log("Returning display list to child");
		
	} else {
		log("Error - no such graph " + srcid);
	}
	return ret;
 }

 window.newGraph = function(par, title) {
	 var ret = new Graph(par);
	 ret.setTitle(title);
	 return ret;
 };

 
function Graph(parent, dl) {
	var me = this;
		
	var displayList = (dl ? dl : new DisplayList());
	
	var rlog10 = 1 / Math.log(10);
	
	var topMargin = 26;
	var leftMargin = 70;
	var bottomMargin = 50;
	
	
	var myid = Math.round(1.e6 * Math.random());
	var nchildren = 0;
	
	if (!window.graphs) {
		window.graphs = {};
	}
	window.graphs[myid] = me;
	
	 
	
	
	var canvas = document.createElement("canvas");
	empty(parent);
	
	parent.style.position = "relative";
	
	parent.appendChild(canvas);
	var sty = canvas.style;
	sty.position = "absolute";
	sty.top = topMargin + "px";
	sty.bottom = bottomMargin + "px";
	sty.left = leftMargin + "px";
	sty.right = "0px";
	
	sty.backgroundColor = "#f0f0f0";
	
	var titleDiv = document.createElement("div");
	var sty = titleDiv.style;
	sty.position = "absolute";
	sty.top = "0px";
	sty.height = topMargin + "px";
	sty.fontSize = "14pt";
	sty.fontWeight = "bold";
	sty.color = "#606060";
	sty.textAlign = "center";
	sty.width = "100%";
	titleDiv.innerHTML = displayList.title;
	parent.appendChild(titleDiv);
	
	
	var xaxisCanvas = document.createElement("canvas");
	parent.appendChild(xaxisCanvas);
	var sty = xaxisCanvas.style;
	sty.position = "absolute";
	sty.bottom = "0px";
	sty.left = "0px";
	sty.right = "0px";
	sty.height = bottomMargin + "px";
  	
	
	var yaxisCanvas = document.createElement("canvas");
	parent.appendChild(yaxisCanvas);
	var sty = yaxisCanvas.style;
	sty.position = "absolute";
	sty.bottom = "0px";
	sty.left = "0px";
	sty.top = topMargin + "px";
	sty.width = leftMargin + "px";	
 	
	
	var ctrlsDiv = document.createElement("div");
	parent.appendChild(ctrlsDiv);
	ctrlsDiv.style.position = "absolute";
	ctrlsDiv.style.right = "0px";
	ctrlsDiv.style.bottom = "0px";
	
	var tbl = document.createElement("table");
	ctrlsDiv.appendChild(tbl);
	tbl.cellPadding = 0;
	tbl.cellSpacing = 0;
	tbl.style.borderCollapse = 'separate';
	var tr = tbl.insertRow(tbl.rows.length);
	var bhelp = rowButton(tr, "?", showHelp, "Help with mouse controls for zoom and pan");
	var bframe = rowButton(tr, "F", frameData, "Frame the data by resetting the ranges");
	var bpop = rowButton(tr, "W", ownWindow, "Open a new Window for just this plot");
	
	var helpDiv = null; 
	
	 
	
	
	var xlims = displayList.xRange();
	var ylims = displayList.yRange();
	
	var wcx = 0.5 * (xlims[0] + xlims[1]);
	var wcy = 0.5 * (ylims[0] + ylims[1]);
	
	var rx = xlims[1] - xlims[0];
	var ry = ylims[1] - ylims[0];
	
	var dpdwx = canvas.clientWidth / rx;
	var dpdwy = canvas.clientHeight / ry;
	
	var hw = 0;
	var hh = 0;
	var canvasHeight = 0;
	var canvasWidth = 0;
	
	
	canvas.style.cursor = "move";
	canvas.onmousedown = down;
	canvas.onmousemove = move;
	canvas.onmouseup = up;
	canvas.oncontextmenu = noop;
	
	if (document.attachEvent) {
		document.attachEvent("onmouseup", up);
		
	} else if (document.addEventListener) {
		document.addEventListener("mouseup", up);
	}
	
	
	if (canvas.addEventListener) {
		canvas.addEventListener('DOMMouseScroll', wheelScrolled, false);
		canvas.addEventListener("mousewheel", wheelScrolled, false);
	}
	
	
	var dragging = false;
	var zooming = false;
	
	var dragged = false;
	
	
	var xdown = 0;
	var ydown = 0;
	
	var wxdown = 0;
	var wydown = 0;
	
	var wcxdown = 0;
	var wcydown = 0;
	
	var dpdwxdown = 0;
	var dpdwydown = 0;
	
	var x0 = 0;
	var y0 = 0;
	
	var axisxmin = 0;
	var axisxmax = 0;
	var axisymin = 0;
	var axisymax = 0;
	
	var xticks = null;
	var yticks = null;
	
	var legendOffset = 0;
	
	if (window.addEventListener) {
		window.addEventListener("resize", windowResized);
	}
	
	applySize();
	
	setTimeout(repaint, 10);
	
	var rpTimeout = null;
	
	
	function addPoint(line, x, y, col) {
		displayList.addPoint(line, x, y, col);
		if (!rpTimeout) {
			rpTimeout = setTimeout(toRepaint, 50);
		}
	}
	me.addPoint = addPoint;
	
	function toRepaint() {
		repaint();
		rpTimeout = null;
		console.log("Repainted...");
	}
	
	 
	me.getDisplayList = function() {
		return displayList;
	}
	
	
	
	function rowButton(tr, lbl, func, tip) {
		var td = tr.insertCell(tr.cells.length);
		var ret = document.createElement("input");
		ret.type = "button";
		td.appendChild(ret);
		ret.value = lbl;
		if (tip) {
			ret.title = tip;
		}
		if (func) {
			ret.onmouseup = func;
		}
		return ret;
	}
	
	
	function showHelp() {
		if (!helpDiv) {
			helpDiv = document.createElement("div");
			helpDiv.innerHTML = "Left click: zoom in<br/>" +
							"Right click : zoom out<br/> " +
							"Left click and drag: pan<br/>" +
							"Scroll wheel: zoom</br> " +
							"Right click and drag: zoom x and y seperately<br/>" + 
							"During zooms the point under the mouse remains fixed.";
			var sty = helpDiv.style;
			sty.backgroundColor = "#fffff0";
			sty.borderRadius = "8px";
			sty.padding = "12px";
			sty.fontSize = "10pt";
			sty.border = "1px solid #a0a0a0";
			sty.position = "absolute";
			sty.bottom = "24px";
			sty.right = "0px";
			sty.width = "180px";
			sty.display = "none";
			
			helpDiv.showing = false;
			div.appendChild(helpDiv);
		}
		
		if (helpDiv.showing) {
			helpDiv.style.display = "none";
			helpDiv.showing = false;
		} else {
			helpDiv.style.display = "block";
			helpDiv.showing = true;
		}
		
	}
	
	
	function frameData() {
		var xlims = displayList.xRange();
		var ylims = displayList.yRange();
		
		setRanges(xlims[0], xlims[1], ylims[0], ylims[1]);
	}
	
	
	function setRanges(xa, xb, ya, yb) {	
		wcx = 0.5 * (xa + xb);
		wcy = 0.5 * (ya + yb);
		
		var rx = 1.2 * (xb - xa);
		var ry = 1.2 * (yb - ya);
		
		dpdwx = canvas.clientWidth / rx;
		dpdwy = canvas.clientHeight / ry;
		xticks = null;
		yticks = null;
		repaint();
	}
	me.setRanges = setRanges;
	
	
	function ownWindow() {
		var wname = "ch" + myid + nchildren;
		nchildren += 1;
		var winref = window.open("", wname, "width=500, height=500, scrollbars=no, toolbar=yes");
		var doc = winref.document;
		
		var htmltxt = "<html><head></head><body style='margin : 0px'>\n";
		htmltxt += "<div style='width : 100%; height : 100%'><graph srcid='" + myid + "'></graph></div>\n";
		htmltxt += "<script src='" + jsSource() + "'></script></body></html>\n";
		
		log("inserting html into doc " + htmltxt);
		
		doc.write(htmltxt);
	}
	
	
	function jsSource() {
	 
		var scripts = docETAG("SCRIPT");

		var ssrc = "";
		for (var i = 0; i < scripts.length; i++) {
			var src = scripts[i].getAttribute("src");
			if (src.indexOf("graph.js") >= 0) {
				ssrc = src;
				break;
			}
		}
		var ret = "";
		if (ssrc == "") {
		   log ("Error - can't find own script");
		} else {
			if (ssrc.indexOf("http") == 0) {
				ret = ssrc;

			} else {
				var url = "" + document.location.href;
				log("url is " + url);
				var ls = url.lastIndexOf("/");
				ret = url.substring(0, ls + 1) + ssrc;
			}
		}
		log("JS url is " + ret);
		return ret;
	}
	
	
	
	function setTitle(str) {
		displayList.title = str;
		empty(titleDiv);
		titleDiv.innerHTML = displayList.title;
	}
	me.setTitle = setTitle;
	
	
	function setXLabel(str) {
		displayList.xlable;
		xticks = null;
		repaint();
	}
	me.setXLabel = setXLabel;
	
	
	function setYLabel(str) {
		displayList.ylabel = str;
		yticks = null;
		repaint();
	}
	me.setYLabel = setYLabel;
	
	
	function windowResized() {
		repaint();
	}
	
	
	
	function noop(e) {
		preventDefault(e);
	}


	function empty(elt) {
		while (elt && elt.hasChildNodes()) {
			elt.removeChild(elt.lastChild);
		}
	}

	
	function preventDefault(e) {
		  if (e.stopPropagation) {
		        e.stopPropagation();
		    } else {
		        e.cancelBubble = true;
		    }
		    if (e.preventDefault) {
		       e.preventDefault();
		    } else {
		       e.returnValue = false;
		    }
	}
	
	
	function updatePos() {
		
		x0 = getPosX(canvas) - document.body.scrollLeft;
		y0 = getPosY(canvas) - document.body.scrollTop;
 	}
	
	
	
	function down(e) {
		xdown = e.clientX;
		ydown = e.clientY;
		
		dpdwxdown = dpdwx;
		dpdwydown = dpdwy;
		
		wcxdown = wcx;
		wcydown = wcy;
		
		
		log("down at " + xdown + " " + ydown);
		
		updatePos();

		wxdown = wopx(xdown - x0);
		wydown = wopy(canvasHeight - (ydown - y0));
	
		
		if (rtButton(e)) {
			zooming = true;
			dragging = false;
		} else {
			dragging = true;
			zooming = false;
		}
		dragged = false;
	
		if (document.attachEvent) {
			document.attachEvent("onmousemove", move);
 				
		} else if (document.addEventListener) {
			document.addEventListener("mousemove", move);
 		}
	}
	
	
	
	function up(e) {
		if (dragged) {
			// done a pan or a zoom
			
		} else {	 		
		var x = e.clientX;
		var y = e.clientY;
		var dx = x - xdown;
		var dy = -1 * (y - ydown);
		var r2 = dx * dx + dy * dy;
		if (r2 < 5) {

			log("Zooming " + x + " " + y);
			
			// zoom instead of moving
			var f = 1.2;
			zoomAbout(x - x0,  canvasHeight - (y - y0), (rtButton(e) ? 1./f : f));
		}		
		}
		dragging = false;
		zooming = false;
		
		
		if (document.detachEvent) {
			document.detachEvent("onmousemove", move);
 				
		} else if (document.removeEventListener) {
			document.removeEventListener("mousemove", move);
 		}
		
	}
	
	
	function wheelScrolled(e) {
		if (x0 == 0) {
			updatePos();
		}
		var x = e.clientX;
		var y = e.clientY;
		
		wxdown = wopx(x - x0);
		wydown = wopy(canvasHeight - (y - y0));
		
		if (e && (e.detail || e.wheelDelta)) {

			var d3 = (e.detail ? e.detail * -1 : e.wheelDelta / 40);

			var dz = d3 / 3;


			var zfn = (100 - 10 * dz) / 100;
			var fac = zfn;
			zoomAbout(x - x0, canvasHeight - (y - y0), fac);
		}
		preventDefault(e);
	}
	
	function move(e) {
		
		if (dragging || zooming) {
		
		if (x0 == 0) {
			updatePos();
		}
		
		
		
		var x = e.clientX;
		var y = e.clientY;
		
		var dx = x - xdown;
		var dy = -1 * (y - ydown);
	 
		if (!dragged) {
			var dr2 = dx * dx + dy * dy;
			if (dr2 > 40) {
				dragged = true;
			}
		}
		
		
		if (dragging) {		
			wcx = wcxdown - dx / dpdwx;
			wcy = wcydown - dy / dpdwy;
		
			repaint();

		
		} else if (zooming) {
	 
		
			var frelX = Math.pow(2, 0.03 * dx);
			var frelY = Math.pow(2, 0.03 * dy);
 			zoomRel(x - x0, canvasHeight - (y - y0), frelX, frelY);
		}
		}
		preventDefault(e);
	}
	
	
	
	function zoomAbout(x, y, fac) {	
		// after zoom, wx will map to   fac * dpdwx * (wx - wcxnew)
		// so 
		// fac * dpdwx * (wx - wcxnew) = dpdwx * (wx - wcx)
		// wcxnew = wx - (wx - wcx) / fac;
		
		wcx = wxdown - (wxdown - wcx) / fac;
		wcy = wydown - (wydown - wcy) / fac;
		
		dpdwx *= fac;
		dpdwy *= fac;
		repaint();
	
	}
	
	function zoomRel(x, y, frelX, frelY) {
		// like zoomAbout, except that the zoom factors are relative to the values
		// when the mouse was clicled, not increments on the current values
		// this is needed for continuous zooms where you don't know how many events a 
		// drag will generate
	 	
		wcx = wxdown - (wxdown - wcxdown) / frelX;
		wcy = wydown - (wydown - wcydown) / frelY;
		
		dpdwx = frelX * dpdwxdown;
		dpdwy = frelY * dpdwydown;
		
		repaint();
	
	}
	
	
	
	
	function wopx(x) {
		// x = hx + dpdwx (w - wcx)
		var ret = wcx + (x - hw) / dpdwx;
		return ret;
	}
	
	
	function wopy(y) {
		var ret = wcy + (y - hh) / dpdwy;
		return ret;
	}
	
	
	
	
	
	
	function rtButton(evt) {
		var ret = false;
		if ((evt.which && evt.which == 3) || (evt.button && evt.button == 2)) {
			ret = true;
		}
		return ret;
	 }
	
	
	function applySize() {
		canvasWidth = parent.clientWidth - leftMargin;
		canvasHeight = parent.clientHeight - topMargin - bottomMargin;
		canvas.width = canvasWidth;
		canvas.height = canvasHeight;
	}
	

	 
	function repaint() {
		canvasWidth = parent.clientWidth - leftMargin;
		canvasHeight = parent.clientHeight - topMargin - bottomMargin;
	 	
		legendOffset = 8;
		
		hw = 0.5 * canvasWidth;
		hh = 0.5 * canvasHeight;
		
		canvas.width = canvasWidth;
		canvas.height = canvasHeight;
		
		
		var xmin = wopx(0);
		var xmax = wopx(canvasWidth);
		var ymin = wopy(0);
		var ymax = wopy(canvasHeight);
		
		var xchanged = false;
		if (xticks == null || xmin != axisxmin || xmax != axisxmax) {
			xchanged = true;
			xticks = tickPoints(xmin, xmax);
			axisxmin = xmin;
			axisxmax = xmax;
			xchanged = true;
 		}
		
		var ychanged = false;
		if (yticks == null || ymin != axisymin || ymax != axisymax) {
			xchanged = true;
			yticks = tickPoints(ymin, ymax);
			axisymin = ymin;
			axisymax = ymax;
			ychanged = true;
		}
		 
		
		var ctx = canvas.getContext("2d");
		ctx.lineCap = "round";
		ctx.lineJoin = "round";
		ctx.strokeStyle = "#e0e0e0";
		
		ctx.beginPath();
		var xp = xticks.points;
		for (var i = 0; i < xp.length; i++) {
			var x = xp[i];
			var px = powx(x) + 0.5;
			ctx.moveTo(px, 0);
			ctx.lineTo(px, canvasHeight);
		}
		var yp = yticks.points;
		for (var i = 0; i < yp.length; i++) {
			var y = yp[i];
			var py = canvasHeight - powy(y) + 0.5;
			ctx.moveTo(0, py);
			ctx.lineTo(canvasWidth, py);
		}
		
		ctx.stroke();
		
		
		
		var lines = displayList.getLines();
	
		for (var i = 0; i < lines.length; i++) {
			var line = lines[i];
			drawLine(ctx, line);
		}
		
		
		
		if (xchanged) {
			drawXAxis(xticks);
		}
		
		if (ychanged) {
			drawYAxis(yticks);
		}
	 
	}
	
	

	function drawLine(ctx, line) {
		var xp = line.xpts;
		var yp = line.ypts;
		var np = line.npts;
		
		if (np > 0) {
			ctx.strokeStyle = line.color;	
			ctx.lineWidth = line.width;
			ctx.beginPath();
			if (line.erase) {
				ctx.globalCompositeOperation = "destination-out";
			} else {
				ctx.globalCompositeOperation = "source-over";
			}
					
			//		ctx.globalAlpha = effop;
			
			ctx.moveTo(hw + dpdwx * (xp[0] - wcx),  hh - dpdwy * (yp[0] - wcy));
			
			for (var ipt = 1; ipt < np; ipt++) {
				ctx.lineTo(hw + dpdwx * (xp[ipt] - wcx),  hh - dpdwy * (yp[ipt] - wcy));
			}
			ctx.stroke();
			
			
			if (line.label) {
				ctx.textAlign = "right";
				ctx.textBaseline = "top";
				ctx.font = "normal 12px sans-serif";
				ctx.fillStyle = line.color;
				
				ctx.fillText(line.label, canvasWidth - 10, legendOffset + 8);
				legendOffset += 16;
			}
		}	
		
		
		
		
	}

	
	function powx(x) {
		var fret = hw + dpdwx * (x - wcx);
		var ret = Math.round(fret);
		return ret;
	}
	
	function powy(y) {
		var ret = Math.round(hh + dpdwy * (y - wcy));
		return ret;
	}
	
	
	function drawXAxis(ticks) {
		xaxisCanvas.width = (leftMargin + canvasWidth);
		xaxisCanvas.height = bottomMargin;
	 		
		var ctx = xaxisCanvas.getContext("2d");
		ctx.strokeStyle = "#a0a0a0";
		ctx.lineWidth = 1;
		ctx.globalCompositeOperation = "source-over";
		ctx.lineCap = "round";
		ctx.lineJoin = "round";
		
		ctx.beginPath();
		
		var xp = ticks.points;
		for (var i = 0; i < xp.length; i++) {
			var x = xp[i];
			var px = powx(x) + 0.5;
			ctx.moveTo(leftMargin + px, 0);
			ctx.lineTo(leftMargin + px, 6);
		}
		ctx.stroke();
		
		ctx.textAlign = "center";
		ctx.textBaseline = "top";
		ctx.font = "normal 12px sans-serif";
		ctx.fillStyle = "#606060";
		
		var lbls = ticks.labels;
		for (var i = 0; i < xp.length; i++) {
			var x = xp[i];
			var px = powx(x) + 0.5;
			ctx.fillText(lbls[i], leftMargin + px, 8);
		}
		
		if (displayList.xlabel) {
			ctx.textBaseline = "bottom";
			ctx.font = "bold 12px sans-serif";
			ctx.fillText(displayList.xlabel, leftMargin + canvasWidth / 2, bottomMargin - 4);
		}
	}
	
	
	
	function drawYAxis(ticks) {
		yaxisCanvas.width = leftMargin;
	
		var yah = bottomMargin + canvasHeight;
		yaxisCanvas.height = yah;
	 		
		yaxisCanvas.style.height = yah + "px";
		
		var ctx = yaxisCanvas.getContext("2d");
		ctx.strokeStyle = "#a0a0a0";
		ctx.lineWidth = 1;
		ctx.globalCompositeOperation = "source-over";
		ctx.lineCap = "round";
		ctx.lineJoin = "round";
		
		ctx.beginPath();
		
		var yp = ticks.points;
		for (var i = 0; i < yp.length; i++) {
			var y = yp[i];
			var py = canvasHeight - powy(y) + 0.5;
			ctx.moveTo(leftMargin - 6, py);
			ctx.lineTo(leftMargin, py);
		}
		ctx.stroke();
		ctx.font = "normal 12px sans-serif";
		ctx.fillStyle = "#606060";
		ctx.textAlign = "right";
		ctx.textBaseline = "middle";
		var lbls = ticks.labels;
		for (var i = 0; i < yp.length; i++) {
			var y = yp[i];
			var py = canvasHeight - powy(y) + 0.5;
			ctx.fillText(lbls[i], leftMargin - 10, py);
		}
		
		if (displayList.ylabel) {
			 ctx.save();
			 // ctx.translate(newx, newy);
			 ctx.rotate(-Math.PI/2);
			 ctx.textAlign = "center";
 			ctx.font = "bold 12px sans-serif";
 			ctx.fillText(displayList.ylabel, -canvasHeight / 2, 10);
			ctx.restore();
		}
		
	}
	
	
	 
	
	function tickPoints(xmin, xmax) {
		var rng = xmax - xmin;
		var ltr = rlog10 * Math.log(rng);
		var fltr = Math.floor(ltr);
		var fac10 = Math.pow(10.0, fltr);
		
		var rs = rng / fac10;
		var dr = 1;
		
		if (rs < 1. || rs > 10.) {
			log("ERROR - scaled range out of range " + rs);
			dr = rs;
			
		} else {
			if (rs < 2.5) {
				dr = 0.2;
			}  else if (rs < 6) {
				dr = 0.5;
			} else {
				dr = 1.;
			}
		}
		
		var xsmin = xmin / fac10;
		var xsmax = xmax / fac10;
		// TODO shouldn't need to be in range for ints here
		var ia = Math.ceil(xsmin / dr);
		var ib = Math.floor(xsmax / dr);
		var ni = ib - ia;
		
		var points = [];
		var labels = [];
		for (var i = 0; i <= ni; i++) {
			var v = dr * (ia + i);
			points.push(v * fac10);
		//	log("Tick " + i + " " + points[points.length - 1]);
			labels.push("" + (v * fac10));
		}
		
		
		var ldr = Math.floor(rlog10 * Math.log(dr * fac10));

		for (var i = 0; i <= ni; i++) {
			var v = points[i];
			
			if (Math.abs(v / rng) < 0.001) {
				labels[i] = "0";
			} else {
				var lv = Math.floor(rlog10 * Math.log(Math.abs(v)));
				
				if (ldr >= 0 && lv <= 3) {
					// it is an int
					labels[i] = "" + Math.round(v);
	
				} else {
					var prec = Math.round(lv - ldr + 1);
					if (prec <= 2) {
						prec = 2;
					}
					var sn = v.toPrecision(prec);
					 
					labels[i] = (v < 0. ? "-" : "") + sn;
				}
		 
			}
		
		}
		
		
		var ret = {};
		ret.points = points;
		ret.labels = labels;
		
		return ret;
	}
	
	
	 
	
	function getPosX(obj) {
		var curleft = 0;
		if (obj.offsetParent) {
			while (obj.offsetParent) {
				curleft += obj.offsetLeft;
				obj = obj.offsetParent;
			}
		} else if (obj.x) {
			curleft += obj.x;
		}
		return Number(curleft);
	}

	function getPosY(obj) {
		var curtop = 0;
		if (obj.offsetParent) {
			while (obj.offsetParent) {
				curtop += obj.offsetTop;
				obj = obj.offsetParent;
			}
		} else if (obj.y) {
			curtop += obj.y;
		}
		return Number(curtop);
	}
 
}




function GraphSet() {
	
	var me = this;

	
	function init() {
		var elts = docETAG("graph");
		
		for (var i = 0; i < elts.length; i++) {
			initGraph(elts[i]);
		}	
	}
	me.init = init;


	
	
	
	function initGraph(elt) {
		var par = elt.parentNode;
		
		var dl = new DisplayList();
		
		var xt = elt.getAttribute("xlabel");
		if (xt) {
			dl.xlabel = xt;
		}
		var yt = elt.getAttribute("ylabel");
		if (yt) {
			dl.ylabel = yt;
		}
		var ttl = elt.getAttribute("title");
		if (ttl) {
			dl.title = ttl;
		}
		
		for (var i = 0; i < elt.childNodes.length; i++) {
			var cn = elt.childNodes[i];
			
		//	log("Node type " + cn.nodeType + " " + cn.tagName);
			
			if (cn.nodeType == 1) {
			
				if (cn.tagName == "LINE") {
					var line = readLine(cn);
					if (line.npts > 0) {						
						dl.addLine(line);
					} else {
						log("Empty line ?");
						log(cn);
					}
					
				} else {
					log("Urecognized tag in graph " + cn.tagName);
				}
			}
		}
				
		
		dl.addLine(makeTestLine());
		
		var srcid = elt.getAttribute("srcid");
		if (srcid && window.opener) {
			dl = window.opener.getDisplayList(srcid);
		}
	
		var g = new Graph(par, dl);
	}
	
	
	
	function makeTestLine() {
		var ret = {};
		ret.color = "#008000";
		ret.width = 1;
		ret.xpts = [];
		ret.ypts = [];
		
		for (var i = 0; i < 10000; i++) {
			var t = 0.01 * i;
			ret.xpts.push(t);
			ret.ypts.push(t * Math.sin(t));
		}
		ret.npts = ret.xpts.length;
		return ret;
	}
	
	
	
	function readLine(elt) {
		var ret = {};
		console.log(elt);
		
		var c = elt.getAttribute("color");
		var w = elt.getAttribute("width");
		ret.color = (c ? c : "#ffffff");
		ret.width = (w ? w : 1);
	
		var lbl = elt.getAttribute("name");
		if (lbl) {
			ret.label = lbl;
		}
		
		for (var i = 0; i < elt.childNodes.length; i++) {
			var cn = elt.childNodes[i];
			if (cn.nodeType == 1) {
			if (cn.tagName == "XVALUES") {
				ret.xpts = readPoints(cn);
				
			} else if (cn.tagName == "YVALUES") {
				ret.ypts = readPoints(cn);
				
			} else {
				log("unrecognized tag " + cn.tagName);
			}
			}
		}
		
		ret.npts = 0;
		if (ret.xpts && ret.ypts) {
			var nx = ret.xpts.length;
			var ny = ret.ypts.length;
			ret.npts = (nx > ny ? ny : nx);
		}
		
		log("read line " + ret.width + " " + ret.color);
		return ret;
	}
	
	
	function readPoints(elt) {
		var ret = [];
		var txt = elt.innerHTML;
		var bits = txt.split(",");
		for (var i = 0; i < bits.length; i++) {
			var d = parseFloat(bits[i]);
			if (isNaN(d)) {
				log("can't parse " + bits[i]);
			} else {
				ret.push(d);
			}
		}
		log("read points, n=" + ret.length);
		return ret;
	}
		
}


function DisplayList() {
	var me = this;
	
	me.title = "Plot Title";
	me.xlabel = "X Axis Label";
	me.ylabel = "Y Axis Label";
	
	var lines = [];
	
	var lineMap = {};
	
	function addLine(line) {
		lines.push(line);
	}
	me.addLine = addLine;
	
	function getLines() {
		return lines;
	}
	me.getLines = getLines;
	
	
	function addPoint(lin, x, y, col) {
		if (!lineMap[lin]) {
			var line = {};
			line.color = col;
			line.id = lin;
			line.name = lin;
			line.xpts = [];
			line.ypts = [];
			line.npts = 0;
			line.width = 1;
			lineMap[lin] = line;
			lines.push(line);
		}
		
		var line = lineMap[lin];
		line.xpts[line.npts] = x;
		line.ypts[line.npts] = y;
		line.npts += 1;
		if (col && !line.color) {
			line.color = col;
		}
	}
	me.addPoint = addPoint;
	
	
	function xRange() {
		var ret = [0, 1];
		if (lines.length > 0 && lines[0].npts > 0) {
			var x = lines[0].xpts[0];
			ret = [x, x];
			for (var i = 0; i < lines.length; i++) {
				pushBounds(ret, lines[i].xpts);
			}
		}
		return ret;
	}
	me.xRange = xRange;
	
	
	function yRange() {
		var ret = [0, 1];
		if (lines.length > 0) {
			var y = lines[0].ypts[0];
			ret = [y, y];
			for (var i = 0; i < lines.length; i++) {
				pushBounds(ret, lines[i].ypts);
			}
		}
		return ret;
	}
	me.yRange = yRange;
	
	
	function pushBounds(bds, pts) {
		for (var i = 0; i < pts.length; i++) {
			if (pts[i] < bds[0]) {
				bds[0] = pts[i];
			}
			if (pts[i] > bds[1]) {
				bds[1] = pts[i];
			}
		}
	}
	
	
}



var graphset =  new GraphSet();
graphset.init();