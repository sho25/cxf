begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|services
operator|.
name|base64
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|CRC32
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
specifier|public
class|class
name|BinaryDataService
block|{
specifier|private
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
name|String
name|verifyDataIntegrity
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|length
parameter_list|,
name|long
name|crc32
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"verifyDataIntegrity(["
operator|+
name|data
operator|.
name|length
operator|+
literal|" bytes of data], "
operator|+
name|length
operator|+
literal|", "
operator|+
name|crc32
operator|+
literal|") called."
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|getStatusForData
argument_list|(
name|data
argument_list|,
name|length
argument_list|,
name|crc32
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"verifyDataIntegrity status: "
operator|+
name|status
argument_list|)
expr_stmt|;
return|return
name|status
return|;
block|}
comment|/**      * @param data      * @param length      * @param crc32      * @return      */
specifier|private
name|String
name|getStatusForData
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|length
parameter_list|,
name|long
name|crc32
parameter_list|)
block|{
name|String
name|status
decl_stmt|;
if|if
condition|(
name|data
operator|.
name|length
operator|!=
name|length
condition|)
block|{
name|status
operator|=
literal|"data.length == "
operator|+
name|data
operator|.
name|length
operator|+
literal|", should be "
operator|+
name|length
expr_stmt|;
block|}
else|else
block|{
name|CRC32
name|computedCrc32
init|=
operator|new
name|CRC32
argument_list|()
decl_stmt|;
name|computedCrc32
operator|.
name|update
argument_list|(
name|data
argument_list|)
expr_stmt|;
if|if
condition|(
name|computedCrc32
operator|.
name|getValue
argument_list|()
operator|!=
name|crc32
condition|)
block|{
name|status
operator|=
literal|"Computed crc32 == "
operator|+
name|computedCrc32
operator|.
name|getValue
argument_list|()
operator|+
literal|", should be "
operator|+
name|crc32
expr_stmt|;
block|}
else|else
block|{
name|status
operator|=
literal|"OK"
expr_stmt|;
block|}
block|}
return|return
name|status
return|;
block|}
block|}
end_class

end_unit

