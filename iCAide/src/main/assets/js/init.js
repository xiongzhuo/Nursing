$(function (){
  var fiveTasks = ReportData.fiveTasks;

  var Math = {
    div: function(a, b){
      if(a == 0 || b == 0){
        return 0;
      }
      return a / b;
    },
    toInt :function(a){
      if(typeof a === 'undefined' || a.length <=0){
        return 0;
      }
      return parseInt(a)
    }
  };


    if(ReportData.department){
        $(".department").text(ReportData.department);
    }
  var types = ["接触患者前","接触患者后","无菌操作前", "接触血液体液后","接触患者周边环境后"];
    var worktypes = ['本院', '实习', '进修', '研究生'];
    var recycle_names = ['二周内','一个月内','三个月内', '半年内','一年内'];
    var equip_examines = ['患者区域洗手设施不足','无洗手液或手消毒液','工作区域洗手设施不够','无干手纸巾、干手机','洗手设施太远','洗手液容器未一次性使用','洗手池和水龙头脏'];
    var findWorktype = function(work_type){
        var n = Math.toInt(work_type)-50;
        if(n >=0 && n<worktypes.length){
            return worktypes[n];
        }
        return '其他';
    }
    var findRecycleName= function(recycle){
        var n = Math.toInt(recycle)-1;
        if(n>=0 && n < recycle_names.length){
            return recycle_names[n];
        }
        return '无记录';
    }
    var findEquipExamines = function(equipexamine){
        var n = Math.toInt(equipexamine)-1;
        if(n>=0 && n < equip_examines.length){
            return equip_examines[n];
        }
        return '无记录';
    }

    var findPpost = function(ppost){
        var post = window.pposts[ppost];
        if(post){
            return post;
        }
        return '其他';
    }

  var getString = function(text){
    if (typeof text === 'undefined'){
      return '无记录';
    }
    if(text.length<=0){
      return '无记录';
    }
    return text;
  }

    //反馈处理
    if(window.Feedbacks){
        (function(){
            $('.feedback_name.records').text(getString(Feedbacks.remark));
            $('.feedback_name.name').text(getString(Feedbacks.feedback_obj));
            if(Feedbacks.is_training==1){
                $('.istraning').text('是');
                $('.training_recycle_name').text(findRecycleName(Feedbacks.training_recycle));
            } else {
                $('.istraning').text('否');
            }

      var haved = false;
      if(Feedbacks.equip_examine){
        var ees = Feedbacks.equip_examine.split(',');
        for(var x in ees){
          var ee = findEquipExamines(ees[x]);
          $('.feedback_name.ee').append(
            '<span>' + ee +'</span>'
          )
          haved = true;
        }
      }

      if(!haved){
        $('.feedback_name.ee').append(
          '<span>无记录</span>'
        )
      }
        })();
    }

    //附件处理
  if(window.IsServer){
    if(window.Attachments){
      (function(){
        var pics = Attachments.pic;
        for(var p in pics){

          $('.remark .pics').append(
            '<img class="small_img" src="'+pics[p].file_name+'" />'
          );

          $('.remark .pics .feedback_name').hide();
        }

        for(var x in Attachments.aud ){
          var aud = Attachments.aud[x];
          $('.remark .records').append(
            '<div class="audio_main" onclick="play_pause('+aud.id+');" >'+
            '<img src="/gkgzjsys/report/image/audio.png" width="140px;" height="60px;" />'+
            '<div class="audio_time"><p>'+aud.time+'</p><p>'+aud.size+' k</p></div>'+
            '<audio class="audio_app" src="'+aud.file_name+'" controls="controls" id="music_'+aud.id+'" preload hidden >'+
            '</div>'
          );

          $('#null_audio').hide();
        }
      })();
    }
  } else {
    $('.remark').empty();
    $('.remark').append("<p class='info'>无附件或附件正在上传中,您可以等待上传完毕在任务列表中查看.</p>")
  }




  //设置柱状图的高度，用50来推算
  var h = 50*fiveTasks.length;
  if(h<300){h = 300;}
  $("#main1").height(""+h);
  console.log($("#main1").height());

  //指征 时机 依从性 正确数  正确率
  function Report1(){
    this.times = 0;
    this.doTimes = 0;
    this.rightTimes = 0;
    this.type = 0;
    this.pname = '';
    this.ppost = 0;

    this.getDoTimesRate = function(){
      if (this.doTimes == 0 || this.times ==0) {return 0;}
      return (this.doTimes * 100 / this.times).toFixed(1);
    }

    this.getRightTimesRate = function() {
      if (this.rightTimes == 0 || this.doTimes ==0) {return 0;}
      return (this.rightTimes * 100 / this.doTimes).toFixed(1);
    }
    this.getTimeType = function() {
      return types[this.type-1];
    }

    this.makeHtml = function() {
      console.log(this.type);
      return "<tr>"+
          "<td>"+this.getTimeType()+"</td>"+
          "<td>"+this.times+"</td>"+
          "<td>"+this.doTimes+"</td>"+
          "<td>"+this.getDoTimesRate()+"%</td>"+
          "<td>"+this.rightTimes+"</td>"+
          "<td>"+this.getRightTimesRate()+"%</td>"+
        "</tr>";
    }

    this.makeSumaryHtml = function() {
          return  "<tr>"+
          "<td></td>"+
          "<td>"+this.times+"</td>"+
          "<td>"+this.doTimes+"</td>"+
          "<td>"+this.getDoTimesRate()+"%</td>"+
          "<td>"+this.rightTimes+"</td>"+
          "<td>"+this.getRightTimesRate()+"%</td>"+
          "</tr>";
    }

    this.makePersonHtml = function() {
          return  "<tr>"+
          "<td>"+this.pname+"</td>"+
          "<td>"+this.times+"</td>"+
          "<td>"+this.doTimes+"</td>"+
          "<td>"+this.getDoTimesRate()+"%</td>"+
          "<td>"+this.rightTimes+"</td>"+
          "<td>"+this.getRightTimesRate()+"%</td>"+
          "</tr>";
    }
  
  this.makePPostHtml = function() {
  return  "<tr>"+
  "<td>"+findPpost(this.ppost)+"</td>"+
  "<td>"+this.times+"</td>"+
  "<td>"+this.doTimes+"</td>"+
  "<td>"+this.getDoTimesRate()+"%</td>"+
  "<td>"+this.rightTimes+"</td>"+
  "<td>"+this.getRightTimesRate()+"%</td>"+
  "</tr>";
    }

    this.setSubTask = function(subTask){
      this.times += 1;
            if(subTask.results!=0){
                this.doTimes += 1;
                if(subTask.results>=1 && subTask.results<=3){
                    this.rightTimes += 1;
                }
            }
    }
  };

  var report1 = {};
  //五个时机
  for(var x in [0,1,2,3,4]){
    report1[x] = new Report1();
    report1[x].type = parseInt(x)+1;
  }

  var setUnruleHtml = function(task, j){
        var subTask = task.subTasks[j];
        var list4 = [];
        var list5 = [];
        var list6 = [];

        var makeUnruleHtml = function(unrules, work_type, ppost, n){
            var color = 'orange';
            if(n/2%2==1){
                color = 'green';
            }
            var s = '<p class="record">'+
                '<span  class="'+color+'" >'+work_type+'</span>'+
                '<span  class="'+color+'" >'+ppost+'</span>';
            for(var x in unrules){
                var rule = unrules[x];
                s += '<span>'+rule["name"]+'</span>';
            }
            s += '</p>';
            s += '<p class="line"></p>';
            return s;
        }

    if(subTask.unrules){
            //4.洗手不规范记录
            //5. 卫生手消毒不规范记录
            //6. 戴手套不规范记录
            var n = $('.unruler'+subTask.results+' .record_main'). children().length;
            var html = makeUnruleHtml(subTask.unrules, findWorktype(task.work_type), findPpost(task.ppost),n);
            $(".unruler"+subTask.results+' .record_main').append(html);
            $('.unruler'+subTask.results+' .feedback_name').hide();
        }
  }

  var totalReport = new Report1();
  for(var i in fiveTasks){
    var task = fiveTasks[i];
    for(var j in task.subTasks){
        var subTask = task.subTasks[j];
            setUnruleHtml(task,j);
  
            var col_types = subTask.col_type.split('');
            for(var x in col_types){
                var col_type = col_types[x];
                var report = report1[col_type];
                if(report){report.setSubTask(subTask);}
            }
            totalReport.setSubTask(subTask);
    }
  }

  for(var x in [0,1,2,3,4]){
    var report = report1[x];
    $(".detail tbody").append($(report.makeHtml()));
  }
  $('.summary tbody').append($(totalReport.makeSumaryHtml()));

  

  require.config({
        paths: {
            echarts: 'http://echarts.baidu.com/build/dist'
        }
    });

  // 使用
    require(
        [
            'echarts',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {

          //按岗位
      var ppReports = {};
      var keys = [];
      for(var i in fiveTasks){
        var task = fiveTasks[i];
        var post = findPpost(task.ppost);
        var report = ppReports[post];
        if(!report){
          report = new Report1();
          ppReports[post] = report;
          keys.push(post);
        }
        report.ppost = task.ppost;
        
        for(var j in task.subTasks){
            var subTask = task.subTasks[j];
                report.setSubTask(subTask);
        }
      }
            console.log("岗位:");
            console.log(ppReports);

      var datas1 = [];
          var datas2 = [];
      for(var i in keys){
        var report = ppReports[keys[i]];
        datas1.push(Math.div(report.doTimes * 100 , report.times).toFixed(0));
        datas2.push(Math.div(report.rightTimes * 100 , report.doTimes).toFixed(0));
            
                $(".ppost tbody").append($(report.makePPostHtml()));
      }


      var h = 100*keys.length;
      if(h<300){h = 300;}
      $("#main2").height(""+h);

            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('main2')); 
            
        var option = {
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:['依从性', '正确率'],
            y: 'top',
            selectedMode: false
        },
        color: ['#fd9c4c','#50e39a'],
        
        calculable : false,
        yAxis : [
            {
                type : 'category',
                // splitLine: false,
                // splitArea: false,
                data : keys,
                axisLabel: {
           //    interval: 0,
           //    formatter:function(val){
            //  return val.split("").join("\n");
            // }
          }
            }
        ],
        xAxis : [
            {
                type : 'value',
                axisLabel: {
                  formatter:function(val){
                    return val + "%";
                  }
                }
            }
        ],
        series : [
             
            {
                name:'依从性',
                type:'bar',
                barWidth: 20,
                itemStyle : { normal: {label : {show: true, formatter: '{c}%'}}},
                data:datas1
            },
            {
                name:'正确率',
                type:'bar',
                barWidth: 20,
                itemStyle : { normal: {label : {show: true, formatter: '{c}%'}}},
                data:datas2
            }
        ]
    };

            // 为echarts对象加载数据 
        myChart.setOption(option); 
            
        }
    );

  //指征
  require(
        [
            'echarts',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {

            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('main3')); 

            var datas1 = [];
            var datas2 = [];
            for(var x in [0,1,2,3,4]){
        var report = report1[x];
        datas1.push(Math.div(report.doTimes * 100 , report.times).toFixed(0));
        datas2.push(Math.div(report.rightTimes * 100 , report.doTimes).toFixed(0)); 
      }

            console.log(datas1);
            console.log(datas2);
        var option = {
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:['依从性', '正确率'],
            y: 'top',
            selectedMode: false
        },
        color: ['#fd9c4c','#50e39a'],
        
        calculable : false,
        yAxis : [
            {
                type : 'category',
                // splitLine: false,
                // splitArea: false,
                data : types,
                axisLabel: {
           //    interval: 0,
           //    formatter:function(val){
            //  return val.split("").join("\n");
            // }
          }
            }
        ],
        xAxis : [
            {
                type : 'value',
                axisLabel: {
                  formatter:function(val){
                    return val + "%";
                  }
                }
            }
        ],
        series : [
             
            {
                name:'依从性',
                type:'bar',
                barWidth: 20,
                itemStyle : { normal: {label : {show: true, formatter: '{c}%'}}},
                data:datas1
            },
            {
                name:'正确率',
                type:'bar',
                barWidth: 20,
                itemStyle : { normal: {label : {show: true, formatter: '{c}%'}}},
                data:datas2
            }
        ]
    };

            // 为echarts对象加载数据 
        myChart.setOption(option); 
            
        }
    );


  //按被观察对象

    // 使用
    require(
        [
            'echarts',
            'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('main'));


            //按岗位
      var ppReports = {};
      var keys = [];
      for(var i in fiveTasks){
        var task = fiveTasks[i];
        var post = findPpost(task.ppost);

        if(!ppReports[post]){
          ppReports[post] = 1;
          keys.push(post);
        } else {
          ppReports[post] +=1;
        }
      }

      console.log(ppReports);
            var datas = [];
            var totalTime = 0;
            for(var x in keys){
        datas.push({name: keys[x], value: ppReports[keys[x]]});
        totalTime += ppReports[keys[x]];
      }
            
            var option = {
          title : {
              // text: '某站点用户访问来源',
              // subtext: '纯属虚构',
              // x:'right'
          },
          tooltip: {
            show: false
          },
          animation: true,
          calculable: false,
          color: ['#fd9d4c','#50e39b','#5e97ff','#8d5ef9','#f95e68'],
          legend: {
            show: true,
            // orient: 'vertical',
            // x: 'left',
            y: 'bottom',
            selectedMode: false,
            data: keys
          },
          series : [
              {
                  name:'访问来源',
                  type:'pie',
                  radius : '55%',
                  center: ['50%', '40%'],
                  itemStyle : {
                      normal : {
                          label : {
                              show : true,
                              position:'outer',
                              formatter: function(val) {
                                return val.name + ''+(val.value*100/totalTime).toFixed(1) + "%";
                              }
                          },
                          labelLine : {
                              show : true
                          }
                      },
                      emphasis : {
                          label : {
                              show : false,
                              position : 'center',
                              textStyle : {
                                  fontSize : '30',
                                  fontWeight : 'bold'
                              }
                          }
                      }
                  },
                  data:datas
              }
          ]
      };
    
            // 为echarts对象加载数据 
            myChart.setOption(option); 
        }
    );

  
  for(var i in fiveTasks){
    var task = fiveTasks[i];
    var report = new Report1();
    report.pname = task.pname;
    for(var j in task.subTasks){
        var subTask = task.subTasks[j];
            report.setSubTask(subTask);
    }

    $(".person-detail tbody").append($(report.makePersonHtml()));
  }



  require(
        [
            'echarts',
            'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('main4'));
            var washTypes = ["洗手",'卫生手消毒', '戴手套'];
            var washRightTimes = [0,0,0];
            var washErrorTimes = [0,0,0];
            var noneTimes = 0;

            var reports = [];
            for(var i in fiveTasks){
              var subTasks = fiveTasks[i].subTasks;
              for(var j in subTasks){
                var subTask = subTasks[j];

                if (subTask.results ==1) {
                  washRightTimes[0] += 1;
                } else if(subTask.results ==1+3){
                  washErrorTimes[0] += 1;
                } else if (subTask.results ==2) {
                  washRightTimes[1] += 1;
                } else if(subTask.results ==5){
                  washErrorTimes[1] += 1;
                } else if (subTask.results ==3) {
                  washRightTimes[2] += 1;
                } else if(subTask.results ==6){
                  washErrorTimes[2] += 1;
                } else {
                  noneTimes += 1;
                }
              }
            }

            var datas = [];
            var totalTime = 0;
            for(var x in washTypes){
        datas.push({name: washTypes[x], value: washRightTimes[x] + washErrorTimes[x] });
        totalTime +=  washRightTimes[x] + washErrorTimes[x];

        // 洗手方式
        // 手卫生次数
        // 正确数
        // 不规范数

        $(".washtype-detail tbody").append(
            '<tr>' + 
            '<td>'+washTypes[x]+'</td>' +
            '<td>'+(washRightTimes[x] + washErrorTimes[x])+'</td>' +
            '<td>'+washRightTimes[x]+'</td>' +
            '<td>'+Math.div(washRightTimes[x]*100, (washRightTimes[x] + washErrorTimes[x])).toFixed(1)+'%</td>' +
            '<td>'+washErrorTimes[x]+'</td>' +
            '</tr>'
          );
      }

      washTypes.push('未采取措施');
      datas.push({name: washTypes[3], value: noneTimes});
      totalTime +=  noneTimes;

      $(".washtype-detail tbody").append(
            '<tr>' + 
            '<td>'+washTypes[3]+'</td>' +
            '<td>-</td>' +
            '<td>-</td>' +
            '<td>-</td>' +
            '<td>'+noneTimes+'</td>' +
            '</tr>'
          );
      

            
            var option = {
          title : {
              // text: '某站点用户访问来源',
              // subtext: '纯属虚构',
              // x:'right'
          },
          tooltip: {
            show: false
          },
          animation: true,
          calculable: false,
          legend: {
            show: true,
            // orient: 'vertical',
            // x: 'left',
            y: 'bottom',
            selectedMode: false,
            data: washTypes
          },
          color: ['#50e39b','#fd9d4c','#5e97ff','#f95e68','#8d5ef9'],
          series : [
              {
                  name:'访问来源',
                  type:'pie',
                  radius : '55%',
                  center: ['50%', '40%'],
                  itemStyle : {
                      normal : {
                          label : {
                              show : true,
                              position:'outer',
                              formatter: function(val) {
                                return val.name + ''+(val.value*100/totalTime).toFixed(1) + "%";
                              }
                          },
                          labelLine : {
                              show : true
                          }
                      },
                      emphasis : {
                          label : {
                              show : false,
                              position : 'center',
                              textStyle : {
                                  fontSize : '30',
                                  fontWeight : 'bold'
                              }
                          }
                      }
                  },
                  data:datas
              }
          ]
      };
    
            // 为echarts对象加载数据 
            myChart.setOption(option); 
        }
    );

});