<template>
  <el-container class="main-container">
    <svg id="svg"/>
    <el-container direction="vertical">
      <el-card v-show="currentNode.name!==''" class="box-card detail-panel" shadow="hover">
        <div slot="header" class="clearfix">
          <span style="font-size: 25px">节点信息</span>
        </div>
        <div class="text item">
          <table style="width:300px;border-spacing: 10px">
            <tbody>
            <tr>
              <td class="detail-info">名称</td>
              <td class="detail-info">{{ currentNode.name }}</td>
            </tr>
            <tr>
              <td class="detail-info">类型</td>
              <td class="detail-info">{{ typeMap.get(currentNode.type) }}</td>
            </tr>
            </tbody>
          </table>

          <el-form label-width="0">
            <el-form-item>
              <el-select v-model="currentType" placeholder="请选择查询关系" class="type-select"
                         :disabled="currentNode.type==='Lesson'">
                <el-option v-for="type in currentRelationTypes" :label="type" :key="type"
                           :value="relationMap.get(type)"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit" class="submit-btn" :disabled="currentNode.type==='Lesson'">
                查找
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="info" plain @click="toDetail" class="submit-btn">查看详情</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
      <el-button type="danger" round @click="reset" style="margin-top: 100px;align-self: center;width:100px;">重置图谱
      </el-button>
    </el-container>
  </el-container>
</template>

<script>
import * as d3 from 'd3'

export default {
  name: 'KG',
  data () {
    return {
      nodeLinks: [],
      nodeList: [],
      nodeNameTexts: [],
      linkNameTexts: [],
      simulation: null,
      currentNode: {'name': '', 'type': ''},
      currentType: '',
      lesson: null,
      currentRelationTypes: [],
      width: 900,
      height: 600,
      graphData: {
        'nodes': [],
        'links': []
      },
      colorMap: new Map([['Lesson', 'Green'], ['Chapter', 'Pink'], ['Knowledge', 'Yellow']]),
      typeMap: new Map([['Lesson', '课程'], ['Chapter', '章节'], ['Knowledge', '知识点']]),
      relationMap: new Map([['包含', 'contain'], ['前驱', 'pre'], ['后继', 'suc']]),
      nodesMap: new Map(),
      linksMap: new Map()
    }
  },
  mounted () {
    this.initGraph(this.graphData)
    this.getLesson()
  },
  methods: {
    initGraph (data) {
      const _this = this
      const links = data.links.map(d => Object.create(d))
      const nodes = data.nodes.map(d => Object.create(d))

      _this.simulation = d3.forceSimulation(nodes)
        .force('link', d3.forceLink(links).id(d => d.id).distance(function (d) {
          if (d.source.type === 'Lesson') {
            return 300
          } else {
            return 50
          }
        }))
        .force('collide', d3.forceCollide().radius(function (d) {
          if (d.type === 'Knowledge') {
            return 50
          } else if (d.type === 'Chapter') {
            return 100
          }
          return 20
        }))
        .force('charge', d3.forceManyBody().strength(-50))
        .force('center', d3.forceCenter(_this.width / 2, _this.height / 2))

      const svg = d3.select('svg')
        .attr('viewBox', [0, 0, _this.width, _this.height])

      svg.call(d3.zoom().on('zoom', function () {
        g.attr('transform', d3.event.transform)
      }))

      svg.append('marker')
        .attr('id', 'positiveMarker')
        .attr('orient', 'auto')
        .attr('stroke-width', 2)
        .attr('markerUnits', 'strokeWidth')
        .attr('markerUnits', 'userSpaceOnUse')
        .attr('viewBox', '0 -5 10 10')
        .attr('refX', 25)
        .attr('refY', 0)
        .attr('markerWidth', 12)
        .attr('markerHeight', 12)
        .append('path')
        .attr('d', 'M 0 -5 L 10 0 L 0 5')
        .attr('fill', '#999')
        .attr('stroke-opacity', 0.6)

      svg.append('marker')
        .attr('id', 'negativeMarker')
        .attr('orient', 'auto')
        .attr('stroke-width', 2)
        .attr('markerUnits', 'strokeWidth')
        .attr('markerUnits', 'userSpaceOnUse')
        .attr('viewBox', '0 -5 10 10')
        .attr('refX', -15)
        .attr('refY', 0)
        .attr('markerWidth', 12)
        .attr('markerHeight', 12)
        .append('path')
        .attr('d', 'M 10 -5 L 0 0 L 10 5')
        .attr('fill', '#999')
        .attr('stroke-opacity', 0.6)

      const g = svg.append('g')

      _this.nodeLinks = g.append('g')
        .attr('stroke', '#999')
        .attr('stroke-opacity', 0.6)
        .selectAll('path')
        .data(links)
        .join('path')
        .attr('stroke-width', 2)
        .attr('class', 'link')
        .attr('id', function (d) {
          if (typeof (d.source) === 'object') {
            return d.source.id + '_' + d.relationship + '_' + d.target.id
          } else {
            return d.source + '_' + d.relationship + '_' + d.target
          }
        })

      _this.linkNameTexts = g.append('g')
        .selectAll('text')
        .data(links)
        .join('text')
        .style('text-anchor', 'middle')
        .style('fill', 'white')
        .style('font-size', '12px')
        .style('font-weight', 'bold')
        .append('textPath')
        .attr(
          'xlink:href', function (d) {
            if (typeof (d.source) === 'object') {
              return '#' + d.source.id + '_' + d.relationship + '_' + d.target.id
            } else {
              return '#' + d.source + '_' + d.relationship + '_' + d.target
            }
          }
        )
        .attr('startOffset', '50%')
        .text(function (d) {
          return d.relationship
        })

      _this.nodeList = g.append('g')
        .selectAll('circle')
        .data(nodes)
        .join('circle')
        .attr('r', 20)
        .attr('class', 'node')
        .attr('fill', _this.color)
        .on('click', _this.selectNode)
        .call(_this.drag(_this.simulation))

      _this.nodeNameTexts = g.append('g')
        .selectAll('text')
        .data(nodes)
        .join('text')
        .text(function (d) {
          return d.name
        })
        .attr('dx', function () {
          return this.getBoundingClientRect().width / -2
        })
        .attr('dy', 40)
        .attr('class', 'nodeName')

      _this.simulation.on('tick', () => {
        _this.nodeLinks
          .attr('d', function (d) {
            if (d.source.x < d.target.x) {
              return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y
            } else {
              return 'M ' + d.target.x + ' ' + d.target.y + ' L ' + d.source.x + ' ' + d.source.y
            }
          })
          .attr('marker-end', function (d) {
            if (d.source.x < d.target.x) {
              return 'url(#positiveMarker)'
            } else {
              return null
            }
          })
          .attr('marker-start', function (d) {
            if (d.source.x < d.target.x) {
              return null
            } else {
              return 'url(#negativeMarker)'
            }
          })

        _this.nodeList
          .attr('cx', d => d.x)
          .attr('cy', d => d.y)

        _this.nodeNameTexts
          .attr('x', d => d.x)
          .attr('y', d => d.y)
      })
    },
    color (d) {
      return this.colorMap.get(d.type)
    },
    drag (simulation) {
      function dragStarted (d) {
        if (!d3.event.active) {
          simulation.alphaTarget(0.3).restart()
          d.fx = d.x
          d.fy = d.y
        }
      }

      function dragged (d) {
        d.fx = d3.event.x
        d.fy = d3.event.y
      }

      function dragEnded (d) {
        if (!d3.event.active) {
          simulation.alphaTarget(0)
          if (d.type === 'Lesson') {
            d.fx = null
            d.fy = null
          }
        }
      }

      return d3.drag()
        .on('start', dragStarted)
        .on('drag', dragged)
        .on('end', dragEnded)
    },
    updateGraph (data) {
      const _this = this
      const links = data.links
      const nodes = data.nodes

      _this.nodeLinks = _this.nodeLinks
        .data(links)
        .enter()
        .append('path')
        .attr('stroke', '#999')
        .attr('stroke-opacity', 0.6)
        .attr('stroke-width', 2)
        .merge(_this.nodeLinks)
        .attr('id', function (d) {
          if (typeof (d.source) === 'object') {
            return d.source.id + '_' + d.relationship + '_' + d.target.id
          } else {
            return d.source + '_' + d.relationship + '_' + d.target
          }
        })
        .attr('class', 'link')

      _this.linkNameTexts = _this.linkNameTexts
        .data(links)
        .enter()
        .append('text')
        .style('text-anchor', 'middle')
        .style('fill', 'white')
        .style('font-size', '12px')
        .style('font-weight', 'bold')
        .append('textPath')
        .attr(
          'xlink:href', function (d) {
            if (typeof (d.source) === 'object') {
              return '#' + d.source.id + '_' + d.relationship + '_' + d.target.id
            } else {
              return '#' + d.source + '_' + d.relationship + '_' + d.target
            }
          }
        )
        .attr('startOffset', '50%')
        .merge(_this.linkNameTexts)
        .text(function (d) {
          return d.relationship
        })

      _this.nodeList = _this.nodeList
        .data(nodes)
        .enter()
        .append('circle')
        .attr('r', 20)
        .attr('class', 'node')
        .attr('fill', _this.color)
        .on('click', _this.selectNode)
        .merge(_this.nodeList)
        .call(_this.drag(_this.simulation))

      this.nodeNameTexts = _this.nodeNameTexts
        .data(nodes)
        .enter()
        .append('text')
        .merge(_this.nodeNameTexts)
        .text(function (d) {
          return d.name
        })
        .attr('dx', function () {
          return this.getBoundingClientRect().width / -2
        })
        .attr('dy', 40)
        .attr('class', 'nodeName')

      _this.simulation.nodes(nodes)
      _this.simulation.force('link').links(links)
      _this.simulation.alpha(1).restart()
    },
    selectNode (d) {
      this.currentType = ''
      this.currentNode = d
      if (d.type === 'Chapter') {
        this.currentRelationTypes = ['包含']
        this.$store.commit('setChapter', d)
      } else if (d.type === 'Knowledge') {
        this.currentRelationTypes = ['前驱', '后继']
      }
    },
    getLesson () {
      this.$axios.get('/lesson/get?name=Java程序设计'
      )
        .then(resp => {
          this.graphData.nodes.push(resp.data)
          this.nodesMap.set(resp.data.id, resp.data)
          this.lesson = resp.data
          this.$store.commit('setLesson', this.lesson)
          this.getAllChapters()
        })
        .catch(error => {
          console.log(error)
          this.$message({
            message: '图谱获取失败，请稍后重试。',
            type: 'error',
            showClose: true
          })
        })
    },
    getAllChapters () {
      this.$axios.get('/chapter/getAll'
      )
        .then(resp => {
          for (let i in resp.data) {
            this.graphData.nodes.push(resp.data[i])
            this.nodesMap.set(resp.data[i].id, resp.data[i])
            let key = this.lesson.id + '_包含_' + resp.data[i].id
            this.graphData.links.push({'source': this.lesson.id, 'target': resp.data[i].id, 'relationship': '包含'})
            this.linksMap.set(key, {
              'source': this.lesson.id,
              'target': resp.data[i].id,
              'relationship': '包含'
            })
          }
        })
        .catch(error => {
          console.log(error)
          this.$message({
            message: '图谱获取失败，请稍后重试。',
            type: 'error',
            showClose: true
          })
        })
    },
    onSubmit () {
      if (this.currentType === '') {
        return
      }
      if (this.currentNode.type === 'Chapter') {
        this.$axios.get('/chapter/getKnowledge?id=' + this.currentNode.id
        )
          .then(resp => {
            for (let i in resp.data) {
              if (!this.nodesMap.has(resp.data[i].id)) {
                this.graphData.nodes.push(resp.data[i])
                this.nodesMap.set(resp.data[i].id, resp.data[i])
              }
              let key = this.currentNode.id + '_包含_' + resp.data[i].id
              let value = {
                'source': this.currentNode.id,
                'target': resp.data[i].id,
                'relationship': '包含'
              }
              if (!this.linksMap.has(key)) {
                this.graphData.links.push(value)
                this.linksMap.set(key, value)
              }
            }
          })
          .catch(error => {
            console.log(error)
            this.$message({
              message: '图谱获取失败，请稍后重试。',
              type: 'error',
              showClose: true
            })
          })
      } else if (this.currentNode.type === 'Knowledge') {
        if (this.currentType === 'pre') {
          this.$axios.get('/knowledge/getPre?id=' + this.currentNode.id
          )
            .then(resp => {
              for (let i in resp.data) {
                if (!this.nodesMap.has(resp.data[i].id)) {
                  this.graphData.nodes.push(resp.data[i])
                  this.nodesMap.set(resp.data[i].id, resp.data[i])
                }
                let key = resp.data[i].id + '_前驱_' + this.currentNode.id
                let value = {
                  'source': resp.data[i].id,
                  'target': this.currentNode.id,
                  'relationship': '前驱'
                }
                if (!this.linksMap.has(key)) {
                  this.graphData.links.push(value)
                  this.linksMap.set(key, value)
                }
                key = resp.data[i].parentID + '_包含_' + resp.data[i].id
                value = {
                  'source': resp.data[i].parentID,
                  'target': resp.data[i].id,
                  'relationship': '包含'
                }
                if (!this.linksMap.has(key)) {
                  this.graphData.links.push(value)
                  this.linksMap.set(key, value)
                }
              }
            })
            .catch(error => {
              console.log(error)
              this.$message({
                message: '图谱获取失败，请稍后重试。',
                type: 'error',
                showClose: true
              })
            })
        } else if (this.currentType === 'suc') {
          this.$axios.get('/knowledge/getSuc?id=' + this.currentNode.id
          )
            .then(resp => {
              for (let i in resp.data) {
                if (!this.nodesMap.has(resp.data[i].id)) {
                  this.graphData.nodes.push(resp.data[i])
                  this.nodesMap.set(resp.data[i].id, resp.data[i])
                }
                let key = this.currentNode.id + '_前驱_' + resp.data[i].id
                let value = {
                  'source': this.currentNode.id,
                  'target': resp.data[i].id,
                  'relationship': '前驱'
                }
                if (!this.linksMap.has(key)) {
                  this.graphData.links.push(value)
                  this.linksMap.set(key, value)
                }
                key = resp.data[i].parentID + '_包含_' + resp.data[i].id
                value = {
                  'source': resp.data[i].parentID,
                  'target': resp.data[i].id,
                  'relationship': '包含'
                }
                if (!this.linksMap.has(key)) {
                  this.graphData.links.push(value)
                  this.linksMap.set(key, value)
                }
              }
            })
            .catch(error => {
              console.log(error)
              this.$message({
                message: '图谱获取失败，请稍后重试。',
                type: 'error',
                showClose: true
              })
            })
        }
      }
    },
    toDetail () {
      if (this.currentNode.type === 'Chapter') {
        this.$store.commit('setChapter', this.currentNode)
        this.$router.push('/main/chapter')
      } else if (this.currentNode.type === 'Knowledge') {
        this.$axios.get('/knowledge/get?id=' + this.currentNode.id
        )
          .then(resp => {
            this.$store.commit('setKnowledge', resp.data)
            this.$router.push('/main/knowledge')
          })
          .catch(error => {
            console.log(error)
            this.$message({
              message: '知识点获取失败，请稍后重试。',
              type: 'error',
              showClose: true
            })
          })
      } else if (this.currentNode.type === 'Lesson') {
        this.$store.commit('setLesson', this.lesson)
        this.$router.push('/main/lesson')
      }
    },
    reset () {
      d3.select('svg').remove()
      d3.select('.main-container').insert('svg', 'section').attr('id', 'svg')
      this.nodeLinks = []
      this.nodeList = []
      this.nodeNameTexts = []
      this.linkNameTexts = []
      this.nodesMap = new Map()
      this.linksMap = new Map()
      this.currentNode = {'name': '', 'type': ''}
      this.currentType = ''
      this.currentRelationTypes = []
      this.graphData = {
        'nodes': [],
        'links': []
      }
      this.initGraph(this.graphData)
      this.getLesson()
    }
  },
  watch: {
    graphData: {
      deep: true,
      handler () {
        this.updateGraph(this.graphData)
      }
    }
  }
}
</script>

<style>
.main-container {
  background-color: #F2F6FC;
  height: 700px;
  margin-right: 50px;
  overflow-x: hidden;
}

#svg {
  width: 1150px;
  height: 650px;
  border: 3px solid #2c3e50;
  border-radius: 8px;
  margin: 30px 10px 0 30px;
  background: #7ecbff repeating-linear-gradient(30deg,
  hsla(0, 0%, 100%, .1), hsla(0, 0%, 100%, .1) 15px,
  transparent 0, transparent 30px);
}

.node {
  stroke: #fff;
  stroke-width: 2;
  cursor: pointer;
}

.node:hover {
  stroke-width: 5;
}

.detail-panel {
  border-radius: 4px;
  cursor: pointer;
  width: 350px;
  height: 400px;
  margin: 30px;
}

.detail-info {
  width: 50%;
  text-align: left;
  font-size: 18px;
  font-weight: bolder;
}

.type-select {
  margin-top: 25px;
  height: 20px;
}

.submit-btn {
  width: 100%;
  height: 30px;
  line-height: 0;
  margin-top: 0;
}
</style>
