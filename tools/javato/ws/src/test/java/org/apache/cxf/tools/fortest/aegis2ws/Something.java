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
name|tools
operator|.
name|fortest
operator|.
name|aegis2ws
package|;
end_package

begin_comment
comment|/**  * Test data type for Aegis in java2ws  */
end_comment

begin_class
specifier|public
class|class
name|Something
block|{
comment|// the .aegis.xml file sets no special properties on the following.
specifier|private
name|String
name|multiple
decl_stmt|;
comment|// the .aegis.xml file sets the following to minOccurs=1.
specifier|private
name|String
name|singular
decl_stmt|;
comment|/**      *  @return Returns the multiple.      */
specifier|public
name|String
name|getMultiple
parameter_list|()
block|{
return|return
name|multiple
return|;
block|}
comment|/**      * @param multiple The multiple to set.      */
specifier|public
name|void
name|setMultiple
parameter_list|(
name|String
name|multiple
parameter_list|)
block|{
name|this
operator|.
name|multiple
operator|=
name|multiple
expr_stmt|;
block|}
comment|/**      * @return Returns the singular.      */
specifier|public
name|String
name|getSingular
parameter_list|()
block|{
return|return
name|singular
return|;
block|}
comment|/**      * @param singular The singular to set.      */
specifier|public
name|void
name|setSingular
parameter_list|(
name|String
name|singular
parameter_list|)
block|{
name|this
operator|.
name|singular
operator|=
name|singular
expr_stmt|;
block|}
block|}
end_class

end_unit

