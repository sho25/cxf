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
name|ParsingEvent
block|{
specifier|private
name|int
name|event
decl_stmt|;
specifier|private
name|QName
name|name
decl_stmt|;
specifier|private
name|String
name|value
decl_stmt|;
specifier|public
name|ParsingEvent
parameter_list|(
name|int
name|event
parameter_list|,
name|QName
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|event
operator|=
name|event
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|StringBuffer
argument_list|()
operator|.
name|append
argument_list|(
literal|"Event("
argument_list|)
operator|.
name|append
argument_list|(
name|event
argument_list|)
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * @return Returns the event.      */
specifier|public
name|int
name|getEvent
parameter_list|()
block|{
return|return
name|event
return|;
block|}
comment|/**      * @return Returns the name.      */
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * @return Returns the value.      */
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

