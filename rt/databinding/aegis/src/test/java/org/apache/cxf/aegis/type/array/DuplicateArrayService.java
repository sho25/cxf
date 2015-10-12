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
name|type
operator|.
name|array
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_interface
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"DuplicateArray"
argument_list|,
name|targetNamespace
operator|=
literal|"urn:org.apache.cxf.aegis.type.java5"
argument_list|)
specifier|public
interface|interface
name|DuplicateArrayService
block|{
annotation|@
name|WebMethod
argument_list|()
name|DuplicateArrayReturnItem
index|[]
name|lookup
parameter_list|(
name|String
name|indexid
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
name|List
argument_list|<
name|List
argument_list|<
name|DuplicateArrayReturnItem
argument_list|>
argument_list|>
name|lookupBatch
parameter_list|(
name|String
name|indexid
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
name|Foo
argument_list|<
name|String
argument_list|>
name|doFoo
parameter_list|(
name|Foo
argument_list|<
name|Integer
argument_list|>
name|foo
parameter_list|)
function_decl|;
class|class
name|Foo
parameter_list|<
name|T
parameter_list|>
block|{
name|List
argument_list|<
name|T
argument_list|>
name|list
decl_stmt|;
specifier|public
name|void
name|setList
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|l
parameter_list|)
block|{
name|list
operator|=
name|l
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|getList
parameter_list|()
block|{
return|return
name|list
return|;
block|}
block|}
block|}
end_interface

end_unit

