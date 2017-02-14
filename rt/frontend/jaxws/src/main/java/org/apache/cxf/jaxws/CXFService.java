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
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceFeature
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|spi
operator|.
name|ServiceDelegate
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
name|Bus
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
name|common
operator|.
name|util
operator|.
name|ReflectionUtil
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CXFService
extends|extends
name|Service
block|{
name|ServiceImpl
name|impl
decl_stmt|;
specifier|protected
name|CXFService
parameter_list|(
name|URL
name|wsdlURL
parameter_list|,
name|QName
name|serviceName
parameter_list|)
block|{
name|super
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
name|impl
operator|=
name|findDelegate
argument_list|()
expr_stmt|;
name|impl
operator|.
name|initialize
argument_list|(
literal|null
argument_list|,
name|wsdlURL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CXFService
parameter_list|(
name|Bus
name|b
parameter_list|,
name|URL
name|wsdlURL
parameter_list|,
name|QName
name|serviceName
parameter_list|)
block|{
name|super
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
name|impl
operator|=
name|findDelegate
argument_list|()
expr_stmt|;
name|impl
operator|.
name|initialize
argument_list|(
name|b
argument_list|,
name|wsdlURL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CXFService
parameter_list|(
name|URL
name|wsdlURL
parameter_list|,
name|QName
name|serviceName
parameter_list|,
name|WebServiceFeature
modifier|...
name|f
parameter_list|)
block|{
name|super
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
name|impl
operator|=
name|findDelegate
argument_list|()
expr_stmt|;
name|impl
operator|.
name|initialize
argument_list|(
literal|null
argument_list|,
name|wsdlURL
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CXFService
parameter_list|(
name|Bus
name|b
parameter_list|,
name|URL
name|wsdlURL
parameter_list|,
name|QName
name|serviceName
parameter_list|,
name|WebServiceFeature
modifier|...
name|f
parameter_list|)
block|{
name|super
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
name|impl
operator|=
name|findDelegate
argument_list|()
expr_stmt|;
name|impl
operator|.
name|initialize
argument_list|(
name|b
argument_list|,
name|wsdlURL
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ServiceImpl
name|findDelegate
parameter_list|()
block|{
for|for
control|(
name|Field
name|f
range|:
name|ReflectionUtil
operator|.
name|getDeclaredFields
argument_list|(
name|Service
operator|.
name|class
argument_list|)
control|)
block|{
if|if
condition|(
name|ServiceDelegate
operator|.
name|class
operator|.
name|equals
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|ServiceDelegate
name|del
init|=
name|ReflectionUtil
operator|.
name|accessDeclaredField
argument_list|(
name|f
argument_list|,
name|this
argument_list|,
name|ServiceDelegate
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|del
operator|instanceof
name|ServiceImpl
condition|)
block|{
return|return
operator|(
name|ServiceImpl
operator|)
name|del
return|;
block|}
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"Delegate of class "
operator|+
name|del
operator|.
name|getClass
argument_list|()
operator|+
literal|" is not a CXF delegate.  "
operator|+
literal|" Check the classpath to make sure CXF is loaded first."
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"Could not find CXF service delegate"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

