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
name|jaxrs
operator|.
name|ext
operator|.
name|provider
operator|.
name|aegis
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

begin_import
import|import
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
name|DefaultTypeCreator
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|NoNamespaceTypeCreator
extends|extends
name|DefaultTypeCreator
block|{
annotation|@
name|Override
specifier|protected
name|QName
name|createQName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|javaType
parameter_list|)
block|{
comment|// no namespace.
name|QName
name|defQname
init|=
name|super
operator|.
name|createQName
argument_list|(
name|javaType
argument_list|)
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|defQname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

