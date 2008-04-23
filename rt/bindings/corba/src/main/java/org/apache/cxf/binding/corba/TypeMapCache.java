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
name|binding
operator|.
name|corba
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|utils
operator|.
name|CorbaUtils
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|TypeMappingType
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
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|TypeMapCache
block|{
specifier|private
specifier|static
specifier|final
name|String
name|KEY
init|=
name|CorbaTypeMap
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|private
name|TypeMapCache
parameter_list|()
block|{
comment|//utility class
block|}
specifier|public
specifier|static
name|CorbaTypeMap
name|get
parameter_list|(
name|ServiceInfo
name|service
parameter_list|)
block|{
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|service
init|)
block|{
name|CorbaTypeMap
name|map
init|=
name|service
operator|.
name|getProperty
argument_list|(
name|KEY
argument_list|,
name|CorbaTypeMap
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|TypeMappingType
argument_list|>
name|corbaTypes
init|=
name|service
operator|.
name|getDescription
argument_list|()
operator|.
name|getExtensors
argument_list|(
name|TypeMappingType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|corbaTypes
operator|!=
literal|null
condition|)
block|{
name|map
operator|=
name|CorbaUtils
operator|.
name|createCorbaTypeMap
argument_list|(
name|corbaTypes
argument_list|)
expr_stmt|;
name|service
operator|.
name|setProperty
argument_list|(
name|KEY
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

