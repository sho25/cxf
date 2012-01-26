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
name|staxutils
operator|.
name|transform
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
class|class
name|ElementProperty
block|{
specifier|private
name|QName
name|name
decl_stmt|;
specifier|private
name|String
name|text
decl_stmt|;
specifier|private
name|boolean
name|child
decl_stmt|;
specifier|public
name|ElementProperty
parameter_list|(
name|QName
name|name
parameter_list|,
name|String
name|text
parameter_list|,
name|boolean
name|child
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
name|this
operator|.
name|child
operator|=
name|child
expr_stmt|;
block|}
comment|/**       * @return Returns the name.      */
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**       * @return Returns the text.      */
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
comment|/**       * @return Returns the child.      */
specifier|public
name|boolean
name|isChild
parameter_list|()
block|{
return|return
name|child
return|;
block|}
block|}
end_class

end_unit

