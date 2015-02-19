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
name|sts
operator|.
name|event
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|sts
operator|.
name|event
operator|.
name|map
operator|.
name|MapEventLogger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|PatternLayout
import|;
end_import

begin_class
specifier|public
class|class
name|LoggerPatternLayoutLog4J
extends|extends
name|PatternLayout
block|{
specifier|private
name|String
name|header
decl_stmt|;
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|header
parameter_list|)
block|{
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getHeader
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|header
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|header
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
return|;
block|}
name|MapEventLogger
name|ll
init|=
operator|new
name|MapEventLogger
argument_list|()
decl_stmt|;
name|StringBuilder
name|line
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|item
range|:
name|ll
operator|.
name|getFieldOrder
argument_list|()
control|)
block|{
name|line
operator|.
name|append
argument_list|(
name|item
argument_list|)
operator|.
name|append
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
block|}
return|return
name|line
operator|.
name|toString
argument_list|()
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

