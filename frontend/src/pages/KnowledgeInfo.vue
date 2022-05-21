<template>
  <div v-html="$store.state.knowledge.info" id="info-container"/>
</template>

<script>
export default {
  name: 'KnowledgeInfo'
}
</script>

<style scoped>
#info-container {
  width: 1000px;
  margin-left: 100px;

  font-size: 1.2em;
  text-align: left;
}

#info-container >>> p {
  word-wrap: break-word;
  word-break: break-all;
  font-size: 15px;
  line-height: 2em;
}

#info-container >>> ul {
  margin-top: 1em;
  margin-bottom: 1em;
  line-height: 140%;
}

#info-container >>> ul li {
  font-size: 15px;
  word-break: break-all;
  margin: 0 0 1em 1em;
  line-height: 1.5em;
}

#info-container >>> .marked {
  padding: 0.2em;
  background-color: #eceae6;
  border-radius: 3px;
  font-weight: bold;
  font-family: Consolas, monospace;
}

#info-container >>> pre {
  margin: 15px auto;
  font: 12px/20px Consolas, monospace;
  white-space: pre-wrap;
  word-break: break-all;
  word-wrap: break-word;
  border: 1px solid #ddd;
  border-left-width: 4px;
  background: #fbfbfb;
  padding: 10px 15px;
}

#info-container >>> h3 {
  font-size: 18px;
  margin: 8px 0;
}

#info-container >>> table.reference {
  border-collapse: collapse;
  width: 100%;
  margin-bottom: 4px;
  margin-top: 4px;
  background-color: white;
}

#info-container >>> table.reference th {
  color: white;
  background-color: #555;
  border: 1px solid #555;
  font-size: 15px;
  padding: 3px;
  vertical-align: top;
  text-align: left;
}

#info-container >>> table.reference td {
  line-height: 2em;
  min-width: 24px;
  border: 1px solid #d4d4d4;
  padding: 7px 5px;
  vertical-align: top;
  font-size: 15px;
}

</style>
