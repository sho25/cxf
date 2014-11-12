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
name|exception
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_class
annotation|@
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebFault
annotation|@
name|XmlType
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/test/HelloService"
argument_list|,
name|name
operator|=
literal|"MyException2"
argument_list|,
name|propOrder
operator|=
block|{
literal|"summary"
block|,
literal|"from"
block|,
literal|"id"
block|,
literal|"message"
block|}
argument_list|)
specifier|public
class|class
name|MyException2
extends|extends
name|NonTransientMessageException
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|8575109064272599936L
decl_stmt|;
specifier|private
name|String
name|summary
decl_stmt|;
specifier|private
name|String
name|from
decl_stmt|;
specifier|public
name|MyException2
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSummary
parameter_list|(
name|String
name|summary
parameter_list|)
block|{
name|this
operator|.
name|summary
operator|=
name|summary
expr_stmt|;
block|}
specifier|public
name|void
name|setFrom
parameter_list|(
name|String
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
specifier|public
name|String
name|getSummary
parameter_list|()
block|{
return|return
name|summary
return|;
block|}
specifier|public
name|String
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
block|}
end_class

end_unit

