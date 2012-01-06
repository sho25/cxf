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
name|jaxb
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|databinding
operator|.
name|AbstractWrapperHelper
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|JAXBWrapperHelper
extends|extends
name|AbstractWrapperHelper
block|{
specifier|protected
specifier|final
name|Method
name|jaxbObjectMethods
index|[]
decl_stmt|;
specifier|protected
specifier|final
name|Object
name|objectFactory
decl_stmt|;
specifier|protected
name|JAXBWrapperHelper
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|wt
parameter_list|,
name|Method
index|[]
name|sets
parameter_list|,
name|Method
index|[]
name|gets
parameter_list|,
name|Method
name|jaxbs
index|[]
parameter_list|,
name|Field
index|[]
name|f
parameter_list|,
name|Object
name|of
parameter_list|)
block|{
name|super
argument_list|(
name|wt
argument_list|,
name|sets
argument_list|,
name|gets
argument_list|,
name|f
argument_list|)
expr_stmt|;
name|jaxbObjectMethods
operator|=
name|jaxbs
expr_stmt|;
name|objectFactory
operator|=
name|of
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|createWrapperObject
parameter_list|(
name|Class
name|typeClass
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|typeClass
operator|.
name|newInstance
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|getWrapperObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|getValue
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|in
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
if|if
condition|(
literal|"javax.xml.bind.JAXBElement"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|je
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|method
operator|.
name|invoke
argument_list|(
name|in
argument_list|)
decl_stmt|;
return|return
name|je
operator|==
literal|null
condition|?
name|je
else|:
name|je
operator|.
name|getValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|getPartObject
parameter_list|(
name|int
name|index
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|object
decl_stmt|;
if|if
condition|(
name|jaxbObjectMethods
index|[
name|index
index|]
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|jaxbObjectMethods
index|[
name|index
index|]
operator|.
name|invoke
argument_list|(
name|objectFactory
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

