<template>
  <el-container direction="vertical">
    <div v-html="codes[currentPage].code" id="code-container"/>
    <el-pagination layout="prev, pager, next" :total="codes.length"
                   :page-size="1" hide-on-single-page @current-change="pageChange"/>
  </el-container>
</template>

<script>
export default {
  name: 'KnowledgeCode',
  data () {
    return {
      codes: [{'code': ''}],
      currentPage: 0
    }
  },
  created () {
    this.codes = this.$store.state.knowledge.codes
    document.getElementById('out-container').scrollTop = 0
  },
  methods: {
    pageChange (p) {
      this.currentPage = p - 1
      document.getElementById('out-container').scrollTop = 0
    }
  }
}
</script>

<style scoped>
#code-container {
  width: 1000px;
  margin-left: 100px;

  font-size: 1.2em;
  text-align: left;
}

#code-container >>> p {
  word-wrap: break-word;
  word-break: break-all;
  font-size: 15px;
  line-height: 2em;
}

#code-container >>> ul {
  margin-top: 1em;
  margin-bottom: 1em;
  line-height: 140%;
}

#code-container >>> ul li {
  font-size: 15px;
  word-break: break-all;
  margin: 0 0 1em 1em;
  line-height: 1.5em;
}

#code-container >>> div.example {
  color: Black;
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #d4d4d4;
  background-image: linear-gradient(#fff, #d1cef6 200px);
}

#code-container >>> h2.example {
  color: #65b6fa;
  background-color: transparent;
  margin: 2px 0;
  line-height: 1.8em;
  font-size: 120%;
}

#code-container >>> .example_code {
  line-height: 1.4em;
  background-color: White;
  padding: 5px;
  border: 1px solid #d4d4d4;
  font-size: 100%;
  font-family: Consolas, monospace;
  word-break: break-all;
  word-wrap: break-word;
}

#code-container >>> .hl-main {
  background-color: White;
  font-family: Consolas, monospace;
  white-space: pre-wrap;
  word-break: break-all;
  word-wrap: break-word;
  border: 0;
}

#code-container >>> .hl-reserved {
  color: Green;
}

#code-container >>> .hl-code {
  color: Black;
}

#code-container >>> .hl-identifier {
  color: Blue;
}

#code-container >>> .hl-brackets {
  color: Olive;
}

#code-container >>> .hl-types {
  color: Orange;
}

#code-container >>> .hl-comment {
  color: Gray;
}

#code-container >>> .hl-quotes {
  color: Darkcyan;
}

#code-container >>> .hl-string {
  color: Brown;
}

#code-container >>> pre {
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

</style>
