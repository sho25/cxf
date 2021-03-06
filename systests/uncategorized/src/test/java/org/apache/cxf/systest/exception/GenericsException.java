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
name|systest
operator|.
name|exception
package|;
end_package

begin_class
annotation|@
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebFault
specifier|public
class|class
name|GenericsException
extends|extends
name|Exception
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
name|ObjectWithGenerics
argument_list|<
name|Boolean
argument_list|,
name|Integer
argument_list|>
name|obj
decl_stmt|;
specifier|public
name|ObjectWithGenerics
argument_list|<
name|Boolean
argument_list|,
name|Integer
argument_list|>
name|getObj
parameter_list|()
block|{
return|return
name|obj
return|;
block|}
specifier|public
name|void
name|setObj
parameter_list|(
name|ObjectWithGenerics
argument_list|<
name|Boolean
argument_list|,
name|Integer
argument_list|>
name|obj
parameter_list|)
block|{
name|this
operator|.
name|obj
operator|=
name|obj
expr_stmt|;
block|}
block|}
end_class

end_unit

