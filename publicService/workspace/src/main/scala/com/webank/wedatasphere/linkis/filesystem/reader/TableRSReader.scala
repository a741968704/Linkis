/*
 * Copyright 2019 WeBank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.wedatasphere.linkis.filesystem.reader

import java.util

import com.webank.wedatasphere.linkis.storage.resultset.table.{TableMetaData, TableRecord}

/**
  * Created by johnnwang on 2019/4/16.
  */
@Deprecated
class TableRSReader extends RSReader {
  override def getHead: util.HashMap[String, Object] = {
    val columns = fsReader.getMetaData.asInstanceOf[TableMetaData].columns
    val resultsetMetaDataList = new util.ArrayList[String]()
    columns.foreach(f => resultsetMetaDataList.add(f.toString()))
    //generateMap("metadata", resultsetMetaDataList)
    null
  }

  /**
    * Grab 5000 rows directly here.(这里直接先抓取5000行)
    *
    * @return
    */
  override def getBody: util.ArrayList[util.ArrayList[String]] = {
    val body = new util.ArrayList[util.ArrayList[String]]
    var row = 0
    while (fsReader.hasNext && row <= 5000) {
      val rows = fsReader.getRecord.asInstanceOf[TableRecord].row
      val bodyChild =  new util.ArrayList[String]()
      rows.foreach{
        f => if(f == null) bodyChild.add("NULL") else bodyChild.add(f.toString)
      }
      body.add(bodyChild)
      row += 1
    }
    fsReader.close()
    body
  }
}
