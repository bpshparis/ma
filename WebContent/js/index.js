var datas = []
var $datasTable = $('#DatasTable');
var $navTab = $('#navTab');

var activeTab = "Mails";
var $MailsTab = $("a[href='#Mails']");
var $TATab = $("a[href='#TA']");
var $NLUTab = $("a[href='#NLU']");
var $DSCTab = $("a[href='#DSC']");
var $WVCTab = $("a[href='#WVC']");
var $NLUETab = $("a[href='#NLUE']");
var $NLUCTab = $("a[href='#NLUC']");
var $NLUKTab = $("a[href='#NLUK']");
var $DSCETab = $("a[href='#DSCE']");
var $DSCCTab = $("a[href='#DSCC']");
var $DSCCATab = $("a[href='#DSCCA']");
var $VRTab = $("a[href='#VR']");
var $FRTab = $("a[href='#FR']");
var $TRTab = $("a[href='#TR']");
var $BPSHTab = $("a[href='#BPSH']");

var previousTab;
var $activeSubDatasTable;
var curEl = {name: "", type: ""};

var columns = {
  "index": {field:"index", title: "index", valign:"middle", align: "center", rowspan: 2, colspan: 1, formatter: "indexFormatter", sortable: false},
  "subject": {field:"content", title: "Subject", valign:"middle", align: "center", rowspan: 2, colspan: 1, formatter: "", sortable: false},
  "content": {field:"content", title: "Content", valign:"middle", align: "center", rowspan: 2, colspan: 1, formatter: "", sortable: false},
  "attached": {field:"attached", title: '<i class="glyphicons glyphicons-paperclip"></i>', valign:"middle", align: "center", rowspan: 2, colspan: 1, formatter: "", sortable: false},
  "picture": {field:"picture", title: '<i class="glyphicons glyphicons-picture"></i>', valign:"middle", align: "center", rowspan: 2, colspan: 1, formatter: "picFormatter", sortable: false},
  "face": {field:"face", title: '<i class="glyphicons glyphicons-user"></i>', valign:"middle", align: "center", rowspan: 2, colspan: 1, formatter: "picFormatter", sortable: false},
  "tip": {field:"tip", title: '<i class="glyphicons glyphicons-text-background"></i>', valign:"middle", align: "center", rowspan: 2, colspan: 1, formatter: "picFormatter", sortable: false},
  "language": {field:"language", title: "Language", valign:"middle", align: "center", rowspan: 2, colspan: 1, formatter: "", sortable: false},
  "entities": {field:"entities", title: "Entities", valign:"middle", align: "center", rowspan: 1, colspan: 3, formatter: "", sortable: false},
  "entitiesR1C5": {field:"entities", title: "Entities", valign:"middle", align: "center", rowspan: 1, colspan: 5, formatter: "", sortable: false},
  "emotions": {field:"emotions", title: "Emotions", valign:"middle", align: "center", rowspan: 1, colspan: 5, formatter: "", sortable: false},
  "languages": {field:"languages", title: "Languages", valign:"middle", align: "center", rowspan: 1, colspan: 3, formatter: "", sortable: false},
  "socials": {field:"socials", title: "Socials", valign:"middle", align: "center", rowspan: 1, colspan: 5, formatter: "", sortable: false},
  "categories": {field:"categories", title: "Categories", valign:"middle", align: "center", rowspan: 1, colspan: 3, formatter: "", sortable: false},
  "keywords": {field:"keywords", title: "Keywords", valign:"middle", align: "center", rowspan: 1, colspan: 2, formatter: "", sortable: false},
  "concepts": {field:"concepts", title: "Concepts", valign:"middle", align: "center", rowspan: 1, colspan: 3, formatter: "", sortable: false},
  "classes": {field:"classes", title: "Classes", valign:"middle", align: "center", rowspan: 1, colspan: 3, formatter: "", sortable: false},
  "words": {field:"words", title: "Words", valign:"middle", align: "center", rowspan: 1, colspan: 7, formatter: "", sortable: false},

    "age": {field:"age", title: "Age", valign:"middle", align: "center", rowspan: 1, colspan: 3, formatter: "", sortable: false},
    "genderR1C3": {field:"gender", title: "Gender", valign:"middle", align: "center", rowspan: 1, colspan: 2, formatter: "", sortable: false},
    "identity": {field:"identity", title: "Identity", valign:"middle", align: "center", rowspan: 1, colspan: 3, formatter: "", sortable: false},
    "location": {field:"location", title: "Location", valign:"middle", align: "center", rowspan: 1, colspan: 4, formatter: "", sortable: false},


  "text": {field:"text", title: "Text", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "type": {field:"type", title: "Type", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "relevance": {field:"relevance", title: "Relevance", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "dbpedia_resource": {field:"dbpedia_resource", title: "Resource", valign:"middle", align: "left", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "count": {field:"count", title: "Count", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "anger": {value:"anger", field:"anger", title: "Anger", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "AngerScoreFormatter", sortable: false},
  "disgust": {field:"disgust", title: "Disgust", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "DisgustScoreFormatter", sortable: false},
  "fear": {field:"fear", title: "Fear", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "FearScoreFormatter", sortable: false},
  "joy": {field:"joy", title: "Joy", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "JoyScoreFormatter", sortable: false},
  "sadness": {field:"sadness", title: "Sadness", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "SadnessScoreFormatter", sortable: false},
  "analytical": {field:"analytical", title: "Analytical", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "AnalyticalScoreFormatter", sortable: false},
  "confident": {field:"confident", title: "Confident", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "ConfidentScoreFormatter", sortable: false},
  "tentative": {field:"tentative", title: "Tentative", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "TentativeScoreFormatter", sortable: false},
  "conscientiousness": {field:"conscientiousness_big5", title: "Conscientiousness", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "ConscientiousnessScoreFormatter", sortable: false},
  "openness": {field:"openness_big5", title: "Openness", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "OpennessScoreFormatter", sortable: false},
  "extraversion": {field:"extraversion_big5", title: "Extraversion", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "ExtraversionScoreFormatter", sortable: false},
  "emotionalRange": {field:"emotional_range_big5", title: "Emotional Range", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "EmotionalRangeScoreFormatter", sortable: false},
  "agreeableness": {field:"agreeableness_big5", title: "Agreeableness", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "AgreeablenessScoreFormatter", sortable: false},
  "label": {field:"label", title: "Label", valign:"middle", align: "left", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "score": {field:"score", title: "Score", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "class": {field:"class", title: "Class", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "type_hierarchy": {field:"type_hierarchy", title: "Type Hierarchy", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "document": {field:"document", title: "Document", valign:"middle", align: "center", rowspan: 1, colspan: 2, formatter: "", sortable: false},
  "line_number": {field:"line_number", title: "Line Number", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "word": {field:"word", title: "Word", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},

    "ageMax": {field:"ageMax", title: "Max", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "ageMin": {field:"ageMin", title: "Min", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "ageScore": {field:"ageScore", title: "Score", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "gender": {field:"gender", title: "Gender", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "genderScore": {field:"genderScore", title: "Score", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "identityName": {field:"identityName", title: "Name", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "identityScore": {field:"identityScore", title: "Score", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "identityTypeHierarchy": {field:"identityTypeHierarchy", title: "Type Hierarchy", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "name": {field:"name", title: "Name", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "locationHeight": {field:"locationHeight", title: "Height", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "locationLeft": {field:"locationLeft", title: "Left", valign:"middle", align: "middle", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "locationTop": {field:"locationTop", title: "Top", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "locationWidth": {field:"locationWidth", title: "Width", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
    "subtype": {field:"subtype", title: "Subtype", valign:"middle", align: "left", rowspan: 1, colspan: 1, formatter: "subtypeFormatter", sortable: false},
  "docLabel": {field:"docLabel", title: "Label", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false},
  "docScore": {field:"docScore", title: "Score", valign:"middle", align: "center", rowspan: 1, colspan: 1, formatter: "", sortable: false}

};

function indexFormatter(value, row, index) {
  row.index = index;
  return index;
}

function picFormatter(value, row, index){
  if(value != undefined){
    return [
      '<img src="res/mails/' + value + '" class="img-thumbnail img-rounded img-responsive center-block" alt="' + value + '">'
    ].join();
  }
}

function subtypeFormatter(value, row, index) {
  if(row.disambiguation.subtype){
    // var html = '<ul class="list-inline">';
    var html = '<ul class="">';
    $.each(row.disambiguation.subtype, function(i, st){
      html += '<li>' + st + '</li>';
    });
    html += '</ul>';
    return html;
  }
}

function ConscientiousnessScoreFormatter(value, row, index) {
  return row.social.conscientiousness_big5;
}

function ConscientiousnessScoreFormatter(value, row, index) {
  return row.social.conscientiousness_big5;
}

function OpennessScoreFormatter(value, row, index) {
  return row.social.openness_big5;
}

function ExtraversionScoreFormatter(value, row, index) {
  return row.social.extraversion_big5;
}

function EmotionalRangeScoreFormatter(value, row, index) {
  return row.social.emotional_range_big5;
}

function AgreeablenessScoreFormatter(value, row, index) {
  return row.social.agreeableness_big5;
}

function AnalyticalScoreFormatter(value, row, index) {
  return row.language.analytical;
}

function ConfidentScoreFormatter(value, row, index) {
  return row.language.confident;
}

function TentativeScoreFormatter(value, row, index) {
  return row.language.tentative;
}

function AngerScoreFormatter(value, row, index) {
  return row.emotion.anger;
}

function DisgustScoreFormatter(value, row, index) {
  return row.emotion.disgust;
}

function FearScoreFormatter(value, row, index) {
  return row.emotion.fear;
}

function JoyScoreFormatter(value, row, index) {
  return row.emotion.joy;
}

function SadnessScoreFormatter(value, row, index) {
  return row.emotion.sadness;
}

function hideTabs(){
  $TATab.hide();
  $NLUTab.hide();
  $DSCTab.hide();
  $WVCTab.hide();
  $NLUETab.hide();
  $NLUCTab.hide();
  $NLUKTab.hide();
  $DSCETab.hide();
  $DSCCTab.hide();
  $DSCCATab.hide();
  $VRTab.hide();
  $FRTab.hide();
  $TRTab.hide();
}

function showTabs(){
  $TATab.show();
  $NLUTab.show();
  $DSCTab.show();
  $WVCTab.show();
  $NLUETab.show();
  $NLUCTab.show();
  $NLUKTab.show();
  $DSCETab.show();
  $DSCCTab.show();
  $DSCCATab.show();
  $VRTab.show();
  $FRTab.show();
  $TRTab.show();
}

function SummerizeTable(table) {
  $(table).each(function() {
    $(table).find('td').each(function() {
      var $this = $(this);
      var col = $this.index();
      var html = $this.html();
      var row = $(this).parent()[0].rowIndex;
      var span = 1;
      var cell_above = $($this.parent().prev().children()[col]);

      // look for cells one above another with the same text
      while (cell_above.html() === html) { // if the text is the same
        span += 1; // increase the span
        cell_above_old = cell_above; // store this cell
        cell_above = $(cell_above.parent().prev().children()[col]); // and go to the next cell above
      }

      // if there are at least two columns with the same value,
      // set a new span to the first and hide the other
      if (span > 1) {
        // console.log(span);
        $(cell_above_old).attr('rowspan', span);
        $this.hide();
      }

    });
  });
}

function mergeCells($table, fields, rowspan){
  var size = $table.find('tbody').children().length;;
  console.log("rowspan=" + rowspan);
  console.log("size=" + size);
  for(index = 0; index <= size; index = index + rowspan){
    console.log("index=" + index);
    $.each(fields, function(i, field){
      $table.bootstrapTable('mergeCells', {index: index, field: field, rowspan: rowspan});
    });
  }
}

function loadTable($table, datas, id, fields){
  // $table.bootstrapTable('removeAll');
  if(id == undefined){
    $table.bootstrapTable('load', datas);
  }
  else {
    var rowspan;
    $.each(datas, function(i, data){
      rowspan = data[id].length;
      $table.bootstrapTable('append', data[id]);
    })
  }
  // mergeCells($table, fields, rowspan);
  SummerizeTable($table);
}

$(document)
.ready(function() {
  // $('#DatasToolbar').hide();
  buildTable($datasTable, mailsCols, true, false);
  loadTable($datasTable, datas);
  hideTabs();
  $('#AnalyzeMails').addClass('disabled');
  $('#UploadMails').addClass('disabled');

})
.ajaxStart(function(){
    $("div#Loading").addClass('show');
})
.ajaxStop(function(){
    $("div#Loading").removeClass('show');
});

$navTab.on('show.bs.tab', function(event){
    activeTab = $(event.target).text();         // active tab
		// console.log("Event show.bs.tab: activeTab=" + activeTab);
    previousTab = $(event.relatedTarget).text();  // previous tab
		// console.log("Event show.bs.tab: previousTab=" + previousTab);
});

$datasTable.on('reset-view.bs.table', function(){
  // console.log("++++++++++++++on passe dans reset-view");
  SummerizeTable($datasTable);
  // console.log("activeTab=" + activeTab);
  // console.log("previousTab=" + previousTab);
  // if($activeSubDatasTable != undefined){
  //   var v = $activeSubDatasTable.bootstrapTable('getData');
    // console.log("+++++++++++ $activeSubDatasTable");
    // console.log(v);
    // var $tableRows = $activeSubDatasTable.find('tbody tr');
    // console.log("++++++++++ $tableRows");
    // console.log($tableRows);
  //   $.each(v, function(i, row){
  //   });
  // }
});

$datasTable.on('expand-row.bs.table', function (index, row, $detail) {
  if(!$detail.analysis.ta){
    ShowAlert('Click <i class="glyphicons glyphicons-cogwheels"></i>', 'to start Watson analysis before visiting tabs.', "alert-warning", "bottom");
  }
});

var mailsCols = [];
mailsCols.push({field:"index", title: "index", formatter: "indexFormatter", sortable: false});
mailsCols.push({field:"subject", title: "Subject", formatter: "", sortable: true});
mailsCols.push({field:"content", title: "Content", formatter: "", sortable: true});
mailsCols.push({field:"attached", title: '<i class="glyphicons glyphicons-paperclip"></i>', align: "center"});
mailsCols.push({field:"picture", title: '<i class="glyphicons glyphicons-picture"></i>', formatter: "picFormatter", align: "center"});
mailsCols.push({field:"face", title: '<i class="glyphicons glyphicons-user"></i>', formatter: "picFormatter", align: "center"});
mailsCols.push({field:"tip", title: '<i class="glyphicons glyphicons-text-background"></i>', formatter: "picFormatter", align: "center"});


var taCols = [];
var taRow0 = [];
// taRow0.push(columns.index);
taRow0.push(columns.subject);
taRow0.push(columns.emotions);
taRow0.push(columns.languages);
// taRow0.push(columns.socials);
var taRow1 = [];
taRow1.push(columns.anger);
taRow1.push(columns.disgust);
taRow1.push(columns.fear);
taRow1.push(columns.joy);
taRow1.push(columns.sadness);
taRow1.push(columns.analytical);
taRow1.push(columns.confident);
taRow1.push(columns.tentative);
// taRow1.push(columns.conscientiousness);
// taRow1.push(columns.openness);
// taRow1.push(columns.extraversion);
// taRow1.push(columns.emotionalRange);
// taRow1.push(columns.agreeableness);
taCols.push(taRow0);
taCols.push(taRow1);

var nluECols = [];
var nluERow0 = [];
// nluERow0.push(columns.index);
nluERow0.push(columns.content);
nluERow0.push(columns.language);
nluERow0.push(columns.entities);
nluERow0.push(columns.emotions);
var nluERow1 = [];
nluERow1.push(columns.type);
nluERow1.push(columns.text);
nluERow1.push(columns.relevance);
nluERow1.push(columns.anger);
nluERow1.push(columns.disgust);
nluERow1.push(columns.fear);
nluERow1.push(columns.joy);
nluERow1.push(columns.sadness);
nluECols.push(nluERow0);
nluECols.push(nluERow1);

var nluCCols = [];
var nluCRow0 = [];
// nluCRow0.push(columns.index);
nluCRow0.push(columns.content);
nluCRow0.push(columns.language);
nluCRow0.push(columns.categories);
var nluCRow1 = [];
nluCRow1.push(columns.label);
nluCRow1.push(columns.score);
nluCCols.push(nluCRow0);
nluCCols.push(nluCRow1);

var nluKCols = [];
var nluKRow0 = [];
// nluKRow0.push(columns.index);
nluKRow0.push(columns.content);
nluKRow0.push(columns.language);
nluKRow0.push(columns.keywords);
nluKRow0.push(columns.emotions);
var nluKRow1 = [];
nluKRow1.push(columns.text);
nluKRow1.push(columns.relevance);
nluKRow1.push(columns.anger);
nluKRow1.push(columns.disgust);
nluKRow1.push(columns.fear);
nluKRow1.push(columns.joy);
nluKRow1.push(columns.sadness);
nluKCols.push(nluKRow0);
nluKCols.push(nluKRow1);

var dscECols = [];
var dscERow0 = [];
// dscERow0.push(columns.index);
dscERow0.push(columns.attached);
dscERow0.push(columns.document);
dscERow0.push(columns.entitiesR1C5)
var dscERow1 = [];
dscERow1.push(columns.docLabel);
dscERow1.push(columns.docScore);
dscERow1.push(columns.text);
dscERow1.push(columns.type);
dscERow1.push(columns.relevance);
dscERow1.push(columns.count);
dscERow1.push(columns.subtype);
dscECols.push(dscERow0);
dscECols.push(dscERow1);

var dscCCols = [];
var dscCRow0 = [];
// dscCRow0.push(columns.index);
dscCRow0.push(columns.attached);
dscCRow0.push(columns.document);
dscCRow0.push(columns.concepts)
var dscCRow1 = [];
dscCRow1.push(columns.docLabel);
dscCRow1.push(columns.docScore);
dscCRow1.push(columns.text);
dscCRow1.push(columns.relevance);
dscCRow1.push(columns.dbpedia_resource);
dscCCols.push(dscCRow0);
dscCCols.push(dscCRow1);

var dscCACols = [];
var dscCARow0 = [];
// dscCARow0.push(columns.index);
dscCARow0.push(columns.attached);
dscCARow0.push(columns.document);
dscCARow0.push(columns.categories)
var dscCARow1 = [];
dscCARow1.push(columns.docLabel);
dscCARow1.push(columns.docScore);
dscCARow1.push(columns.label);
dscCARow1.push(columns.score);
dscCACols.push(dscCARow0);
dscCACols.push(dscCARow1);

var vrCols = [];
var vrRow0 = [];
// vrRow0.push(columns.index);
vrRow0.push(columns.picture);
vrRow0.push(columns.classes);
var vrRow1 = [];
vrRow1.push(columns.class);
vrRow1.push(columns.score);
vrRow1.push(columns.type_hierarchy);
vrCols.push(vrRow0);
vrCols.push(vrRow1);

var frCols = [];
var frRow0 = [];
// frRow0.push(columns.index);
frRow0.push(columns.face);
frRow0.push(columns.age);
frRow0.push(columns.genderR1C3);
frRow0.push(columns.identity);
frRow0.push(columns.location);
var frRow1 = [];
frRow1.push(columns.ageMax);
frRow1.push(columns.ageMin);
frRow1.push(columns.ageScore);
frRow1.push(columns.gender);
frRow1.push(columns.genderScore);
frRow1.push(columns.identityName);
frRow1.push(columns.identityScore);
frRow1.push(columns.identityTypeHierarchy);
frRow1.push(columns.locationHeight);
frRow1.push(columns.locationLeft);
frRow1.push(columns.locationTop);
frRow1.push(columns.locationWidth);
frCols.push(frRow0);
frCols.push(frRow1);

var trCols = [];
var trRow0 = [];
// trRow0.push(columns.index);
trRow0.push(columns.tip);
trRow0.push(columns.words);
var trRow1 = [];
trRow1.push(columns.word);
trRow1.push(columns.line_number);
trRow1.push(columns.score);
trRow1.push(columns.locationHeight);
trRow1.push(columns.locationLeft);
trRow1.push(columns.locationTop);
trRow1.push(columns.locationWidth);
trCols.push(trRow0);
trCols.push(trRow1);

//
// vrCols.push({field:"classes", title: "Classes", rowspan: 1, colspan: 1, formatter: "vrClassesFormatter", align: "center"});
//
// function vrClassesFormatter(value, row, index){
//   if (row.analysis.vr != undefined && row.analysis.vr.images[0].classifiers[0].classes.length > 0){
//     var props = ["class", "score", "type_hierarchy"];
//     var data = row.analysis.vr.images[0].classifiers[0].classes;
//     return createTable(data, props);
//   }
// }
//
// function createTable(data, props){
//
//   var $newDiv;
//
//   $newDiv = $('<div>')
//    .html(
//       '<table class="table table-striped"><thead><tr></tr></thead><tbody></tbody></table>'
//     )
//    .addClass('table-responsive');
//
//    $newDiv.find('tbody').append($('<tr class="info">'));
//    $.each(props, function(i, prop){
//      $newDiv.find('tbody').find('tr.info')
//       .append($('<td>')
//        .text(prop)
//       );
//    });
//
//   $.each(data, function(i, c){
//     var line = $('<tr>');
//     $.each(props, function(j, prop){
//       line.append($('<td>')
//         .text(c[prop])
//       );
//     })
//     $newDiv.find('tbody').append(line);
//   });
//
//   console.log($($newDiv).html());
//
//   return $($newDiv).html();
//
// }

// vrCols.push({field:"class", title: "Class", formatter: "vrClassFormatter", sortable: true});
// vrCols.push({field:"score", title: "Score", formatter: "vrScoreFormatter", sortable: true});
// vrCols.push({field:"type_hierarchy", title: "Type Hierarchy", formatter: "vrTypeHierarchyFormatter", sortable: true});

$datasTable.on('reset-view.bs.table', function(){

});

function filter(){
  var v = $datasTable.bootstrapTable('getData');
  var $tableRows = $datasTable.find('tbody tr');
  $.each(v, function(i, row){
    $tableRows.eq(i).show();
  });
  $.each(v, function(i, row){
    if(activeTab == "Discovery" && !row.attached){
      $tableRows.eq(i).hide();
    }
    if(activeTab == "Visual Recognition" && !row.picture){
      $tableRows.eq(i).hide();
    }
    if(activeTab == "Face Recognition" && !row.face){
      $tableRows.eq(i).hide();
    }
    if(activeTab == "Text Recognition" && !row.tip){
      $tableRows.eq(i).hide();
    }
    if(activeTab == "Delivered by Paris BPSH"){
      $tableRows.eq(i).hide();
    }
  });
}

$MailsTab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, mailsCols, true, false);
  loadTable($datasTable, datas);
  // showColumns();
});

$TATab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, taCols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.tav1){
      rows.push(mails.analysis.tav1);
    }
  });
  loadTable($datasTable, rows);
  // filterColumns("subject");
});

$NLUETab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, nluECols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.nlu){
      rows.push(mails.analysis.nlu);
    }
  });
  loadTable($datasTable, rows, "entities");
});

$NLUCTab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, nluCCols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.nlu){
      rows.push(mails.analysis.nlu);
    }
  });
  loadTable($datasTable, rows, "categories");
});

$NLUKTab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, nluKCols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.nlu){
      rows.push(mails.analysis.nlu);
    }
  });
  loadTable($datasTable, rows, "keywords");
});

$DSCETab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, dscECols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.dsc){
      rows.push(mails.analysis.dsc);
    }
  });
  loadTable($datasTable, rows, "entities");
});

$DSCCTab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, dscCCols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.dsc){
      rows.push(mails.analysis.dsc);
    }
  });
  loadTable($datasTable, rows, "concepts");
});

$DSCCATab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, dscCACols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.dsc){
      rows.push(mails.analysis.dsc);
    }
  });
  loadTable($datasTable, rows, "categories");
});

$VRTab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, vrCols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.vr){
      rows.push(mails.analysis.vr);
    }
  });
  loadTable($datasTable, rows, "classes");
});

$FRTab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, frCols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.fr){
      rows.push(mails.analysis.fr);
    }
  });
  loadTable($datasTable, rows, "faces");
});

$TRTab.on('shown.bs.tab', function(e) {
  $datasTable.bootstrapTable("destroy");
  buildTable($datasTable, trCols, false, false);
  var rows = [];
  $.each(datas, function(i, mails){
    if(mails.analysis.tr){
      rows.push(mails.analysis.tr);
    }
  });
  loadTable($datasTable, rows, "words");
});

$BPSHTab.on('shown.bs.tab', function(e) {
    $(".panel").show();
    $("#Toolbar").hide();
    $datasTable.bootstrapTable("destroy");
});

$('#modPicture').on('shown.bs.modal', function() {
    $(this).find('.modal-header').find('.modal-title').empty();
    $(this).find('.modal-body').empty();
    $(this).find('.modal-footer').empty();
    var html;
    var title;
    var protocol = location.protocol;
    var slashes = protocol.concat("//");
    var host = slashes.concat(window.location.hostname);
    var link = '<a href="res/mails/' + curEl.name + '" download>' + curEl.name + '</a>';
    footer = '<h5><strong>If preview not available, download ' + link + ' to view it on your device.</strong></h5>';

    if(curEl.type.match('pic')){
      html='<img src="res/mails/' + curEl.name + '" class="img-rounded img-responsive center-block" alt="' + curEl.name + '">';
    }
    if(curEl.type.match('pdf')){
      html ='<object type="application/pdf" data="res/mails/' + curEl.name + '" width="100%" height="500" frameborder="0" style="border:0"> No Support</object>';
    }

    if(curEl.type.match('doc|docx')){
      html = '<iframe src="http://docs.google.com/gview?url=' + host + '/res/mails/' + curEl.name + '&embedded=true" width="100%" height="500" frameborder="0" style="border:0"></iframe>'
    }

    if(!curEl.type.match('pic|pdf|doc|docx')){
      title = '<strong>No viewer is available.</strong>';
      html = '<a href="res/mails/' + curEl.name + '" download>' + curEl.name + '</a>';
      footer = '<h5><strong>Download file above to view it on your device.</strong></h5>';

    }
    $(this).find('.modal-header').find('.modal-title').append(title);
    $(this).find('.modal-body').append(html);
    $(this).find('.modal-footer').append(footer);
});

function buildTable($el, cols, showToolbar, showPanel){

  showToolbar == true ? $("#Toolbar").show() : $("#Toolbar").hide();
  showPanel == true ? $(".panel").show() : $(".panel").hide();

  $datasTable.show();

  var stickyHeaderOffsetY = 0;

  if ( $('.navbar-fixed-top').css('height') ) {
         stickyHeaderOffsetY = +$('.navbar-fixed-top').css('height').replace('px','');
  }
 if ( $('.navbar-fixed-top').css('margin-bottom') ) {
     stickyHeaderOffsetY += +$('.navbar-fixed-top').css('margin-bottom').replace('px','');
 }

  $el.bootstrapTable({
      columns: cols,
      // url: url,
      // data: data,
      showToggle: false,
      search: true,
      toolbar: $("#Toolbar"),
      toolbarAlign: "left",
      checkboxHeader: false,
      idField: "index",
      stickyHeader: true,
      stickyHeaderOffsetY: stickyHeaderOffsetY + 'px',
      onEditableInit: function(){
        //Fired when all columns was initialized by $().editable() method.
      },
      onEditableShown: function(editable, field, row, $el){
        //Fired when an editable cell is opened for edits.
      },
      onEditableHidden: function(field, row, $el, reason){
        //Fired when an editable cell is hidden / closed.
      },
      onEditableSave: function (field, row, oldValue, editable) {
        //Fired when an editable cell is saved.
        console.log("---------- buildSubTable: onEditableSave -------------");
        console.log("editable=");
        console.log(editable);
        console.log("field=");
        console.log(field);
        console.log("row=");
        console.log(row);
        console.log("oldValue=");
        console.log(oldValue);
        console.log("---------- buildSubTable: onEditableSave -------------");
      },
      onClickCell: function (field, value, row, $element){
        if(field.match('picture|face|tip') && value != undefined){
          // alert('You click on image ' + value)
          curEl.name = value;
          curEl.type = "pic";
          $('#modPicture').modal('toggle');
        }
        if(field.match('attached') && value != undefined){
          // alert('You click on image ' + value)
          curEl.name = value;
          var ext = value.split('.').pop();
          curEl.type = ext.toLowerCase();
          $('#modPicture').modal('toggle');
        }
      }
  });

}

$("#mailsFile").change(function(){
  // $('#UploadMails').removeClass('disabled');
  // ShowAlert('Click <i class="glyphicons glyphicons-send"></i>', 'to upload ' + $("#mailsFile").val() + ' to server.', "alert-success", "bottom");
  UploadMails();
  RemoveAll();

});

function UploadMails(){

    var fileInput = document.getElementById('mailsFile');
    var file = fileInput.files[0];
    var fd = new FormData();
    fd.append('file', file, 'mailsFile.zip');

    $.ajax({
      url: "Analyze",
      type: "POST",
      data: fd,
      enctype: 'multipart/form-data',
      dataType: 'application/zip',
      processData: false,  // tell jQuery not to process the data
      contentType: false   // tell jQuery not to set contentType
    }).done(function( data ) {
    });
    // ShowAlert('UploadMails()', 'Mails uploaded successfully to server.', "alert-success", "bottom");
}


function GetMails(){

  var fd = new FormData();
  var file = new Blob(['file contents'], {type: 'plain/text'});

  fd.append('formFieldName', file, 'fileName.txt');

  $.ajax({
    url: "Analyze",
    type: "POST",
    data: fd,
    enctype: 'multipart/form-data',
    dataType: 'json',
    processData: false,  // tell jQuery not to process the data
    contentType: false,   // tell jQuery not to set contentType

    success: function(data) {
      console.log(data);
      $.each(data, function(i, obj){
        if(i == "MAILS"){
          console.log(obj);
          if(obj && obj.length == 0){
              console.log("no mails to download");
              ShowAlert("GetMails()", "No mail to download.", "alert-info", "bottom");
          }
          else{
            $datasTable.bootstrapTable('load', obj);
            datas = $datasTable.bootstrapTable('getData');
      			ShowAlert("GetMails()", "Mails downloaded successfully.", "alert-success", "bottom");
            $('#AnalyzeMails').removeClass('disabled');
          }
        }
      });
		},
		error: function(data) {
			ShowAlert("GetMails()", "Downloading mails failed.", "alert-danger", "bottom");
		}
  });

}

function AnalyzeMails(){

  if($datasTable.bootstrapTable("getData").length == 0){
    ShowAlert("AnalyzeMails()", "No mail to analyze", "alert-info", "bottom");
  }

  $.ajax({
    type: 'POST',
    url: "Analyze",
    dataType: 'json',
    data: '{"text": "text", "integer": 1, "boolean": false}',

    success: function(data) {
			console.log(data);
			if (data.length == 0) {
				ShowAlert("AnalyzeMails()", "No analysis returned", "alert-info", "bottom");
				// return;
			}
      $.each(data, function(i, obj){
        if(i == "MAILS"){
          // console.log(JSON.stringify(obj));
          // console.log(obj);
          $datasTable.bootstrapTable('load', obj);
        }
      });
      datas = $datasTable.bootstrapTable('getData');
      ShowAlert("AnalyzeMails()", "Mails analyzed successfully.", "alert-success", "bottom");
      showTabs();

  	},
      error: function(data) {
          console.log(data);
          ShowAlert("AnalyzeMails()", "Analysis failed.", "alert-danger", "bottom");
    }

  });

}

function boolFormatter(value, row, index) {

  var icon = value == true ? 'glyphicon-check' : 'glyphicon-unchecked'
  if(value == undefined){
      console.log("****** VALUE *********" + value);
      console.log(row);
      icon = 'glyphicon-unchecked';
  }
  return [
    '<a href="javascript:void(0)">',
    '<i class="glyphicon ' + icon + '"></i> ',
    '</a>'
  ].join('');
}

function duplicateFormatter(value, row, index) {
  return [
      '<a class="duplicate" href="javascript:void(0)" title="Duplicate">',
      '<i class="glyphicon glyphicon-duplicate"></i>',
      '</a>'
  ].join('');
}

function removeFormatter(value, row, index) {
  return [
      '<a class="remove" href="javascript:void(0)" title="Remove">',
      '<i class="glyphicon glyphicon-trash"></i>',
      '</a>'
  ].join('');
}

function updateCell($table, index, field, newValue){

  $table.bootstrapTable("updateCell", {
    index: index,
    field: field,
    value: newValue
  });

}

function updateRow($table, index, row){

  $table.bootstrapTable("updateRow", {
    index: index,
    row: row
  });

}

function AddRow($table, row){

  $table.bootstrapTable("filterBy", {});
	nextIndex = $table.bootstrapTable("getData").length;
	console.log("nextIndex=" + nextIndex);
	$table.bootstrapTable('insertRow', {index: nextIndex, row: row});


}

function ShowAlert(title, message, alertType, area) {

    $('#alertmsg').remove();

    var timeout = 5000;

    if(area == undefined){
      area = "bottom";
    }
    if(alertType.match('warning')){
      area = "bottom";
      timeout = 10000;
    }
    if(alertType.match('danger')){
      area = "bottom";
      timeout = 30000;
    }

    var $newDiv;

    if(alertType.match('alert-success|alert-info')){
      $newDiv = $('<div/>')
       .attr( 'id', 'alertmsg' )
       .html(
          '<h4>' + title + '</h4>' +
          '<p>' +
          message +
          '</p>'
        )
       .addClass('alert ' + alertType + ' flyover flyover-' + area);
    }
    else{
      $newDiv = $('<div/>')
       .attr( 'id', 'alertmsg' )
       .html(
          '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
          '<h4>' + title + '</h4>' +
          '<p>' +
          '<strong>' + message + '</strong>' +
          '</p>'
        )
       .addClass('alert ' + alertType + ' alert-dismissible flyover flyover-' + area);
    }

    $('#Alert').append($newDiv);

    if ( !$('#alertmsg').is( '.in' ) ) {
      $('#alertmsg').addClass('in');

      setTimeout(function() {
         $('#alertmsg').removeClass('in');
      }, timeout);
    }
}

function TestDBConnection() {

    $.ajax({
        type: 'POST',
        url: "TestDBConnection",
        dataType: 'json',

        success: function(data) {
            console.log(data);
            ShowAlert("TestDBConnection()", "Connection to database was successfull.", "alert-success", "bottom");
        },
        error: function(data) {
            console.log(data);
            ShowAlert("TestDBConnection()", "Connection to database failed.", "alert-danger", "bottom");
        }

    });

}

function Reset() {

	var success = "OK";

	$.ajax({
        type: 'POST',
        url: "Reset",
        dataType: 'json',

        success: function(data) {
			success = "OK";
        },
        error: function(data) {
            console.log(data);
   			success = "KO";
        }

    });

	if (success == "KO") {
		ShowAlert("Reset()", "Operation failed.", "alert-danger", "bottom");
	}

  // window.location = window.location.href+'?eraseCache=true';
	location.reload(true);

}

function GetTableData(){
		var data = $datasTable.bootstrapTable("getData");
		console.log("data=");
		console.log(JSON.stringify(data));
    console.log(data);

}

function RemoveAll(){
  $datasTable.bootstrapTable("removeAll");
  $('#AnalyzeMails').addClass('disabled');
  hideTabs();
}

function ExpandAll(){
  $datasTable.bootstrapTable('expandAllRows');
}
