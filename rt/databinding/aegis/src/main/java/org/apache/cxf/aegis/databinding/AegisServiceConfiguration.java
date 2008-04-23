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
name|databinding
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
name|aegis
operator|.
name|util
operator|.
name|NamespaceHelper
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
name|service
operator|.
name|factory
operator|.
name|AbstractServiceConfiguration
import|;
end_import

begin_comment
comment|/**  * Obsolete copy of {@link XFireCompatibilityServiceConfiguration}.  * @deprecated 2.1  */
end_comment

begin_class
specifier|public
class|class
name|AegisServiceConfiguration
extends|extends
name|AbstractServiceConfiguration
block|{
annotation|@
name|Override
specifier|public
name|String
name|getServiceNamespace
parameter_list|()
block|{
name|String
name|ret
init|=
name|super
operator|.
name|getServiceNamespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
condition|)
block|{
name|ret
operator|=
name|NamespaceHelper
operator|.
name|makeNamespaceFromClassName
argument_list|(
name|getServiceFactory
argument_list|()
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"http"
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

