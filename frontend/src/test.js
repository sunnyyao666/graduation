import * as d3 from 'd3'

function move () {
  this.simulation.on('tick', () => {
    this.nodeLinks
      .attr('d', function (d) {
        if (d.source.x < d.target.x) return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y
        else return 'M ' + d.target.x + ' ' + d.target.y + ' L ' + d.source.x + ' ' + d.source.y
      })
      .attr('marker-end', function (d) {
        if (d.source.x < d.target.x) return 'url(#positiveMarker)'
        else return null
      })
      .attr('marker-start', function (d) {
        if (d.source.x < d.target.x) return null
        else return 'url(#negativeMarker)'
      })
    this.nodeList.attr('cx', d => d.x).attr('cy', d => d.y)
    this.nodeNameTexts.attr('x', d => d.x).attr('y', d => d.y)
  })
}
