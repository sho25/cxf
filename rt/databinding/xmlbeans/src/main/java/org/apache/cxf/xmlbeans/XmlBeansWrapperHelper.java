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
name|xmlbeans
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
name|Method
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlbeans
operator|.
name|XmlOptions
import|;
end_import

begin_class
specifier|public
class|class
name|XmlBeansWrapperHelper
extends|extends
name|AbstractWrapperHelper
block|{
specifier|private
name|boolean
name|validate
decl_stmt|;
specifier|public
name|XmlBeansWrapperHelper
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
name|Field
index|[]
name|f
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
block|}
specifier|public
name|void
name|setValidate
parameter_list|(
name|boolean
name|v
parameter_list|)
block|{
name|validate
operator|=
name|v
expr_stmt|;
block|}
specifier|public
name|boolean
name|getValidate
parameter_list|()
block|{
return|return
name|validate
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|createWrapperObject
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
index|[]
init|=
name|typeClass
operator|.
name|getDeclaredClasses
argument_list|()
decl_stmt|;
name|Method
name|newType
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|typeClass
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"addNew"
argument_list|)
condition|)
block|{
name|newType
operator|=
name|method
expr_stmt|;
break|break;
block|}
block|}
name|Object
name|obj
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|cls
control|)
block|{
if|if
condition|(
literal|"Factory"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getSimpleName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|validate
condition|)
block|{
comment|// set the validation option here
name|Method
name|method
init|=
name|c
operator|.
name|getMethod
argument_list|(
literal|"newInstance"
argument_list|,
name|XmlOptions
operator|.
name|class
argument_list|)
decl_stmt|;
name|XmlOptions
name|options
init|=
operator|new
name|XmlOptions
argument_list|()
decl_stmt|;
name|options
operator|.
name|setValidateOnSet
argument_list|()
expr_stmt|;
name|obj
operator|=
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Method
name|method
init|=
name|c
operator|.
name|getMethod
argument_list|(
literal|"newInstance"
argument_list|,
name|NO_CLASSES
argument_list|)
decl_stmt|;
name|obj
operator|=
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|NO_PARAMS
argument_list|)
expr_stmt|;
block|}
comment|// create the value object
name|obj
operator|=
name|newType
operator|.
name|invoke
argument_list|(
name|obj
argument_list|,
name|NO_PARAMS
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
return|return
name|obj
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
throws|throws
name|Exception
block|{
name|Method
name|m
init|=
name|getXMLBeansValueMethod
argument_list|(
name|wrapperType
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|valueClass
init|=
name|getXMLBeansValueType
argument_list|(
name|wrapperType
argument_list|)
decl_stmt|;
comment|// we need get the real Object first
name|method
operator|=
name|wrapperType
operator|.
name|getMethod
argument_list|(
literal|"get"
operator|+
name|valueClass
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|NO_CLASSES
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|method
operator|=
name|wrapperType
operator|.
name|getMethod
argument_list|(
literal|"get"
operator|+
name|m
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
argument_list|,
name|NO_CLASSES
argument_list|)
expr_stmt|;
block|}
return|return
name|method
operator|.
name|invoke
argument_list|(
name|object
argument_list|,
name|NO_PARAMS
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Method
name|getXMLBeansValueMethod
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|wrapperType
parameter_list|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|wrapperType
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"addNew"
argument_list|)
condition|)
block|{
return|return
name|method
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getXMLBeansValueType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|wrapperType
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|result
init|=
name|wrapperType
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|wrapperType
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"addNew"
argument_list|)
condition|)
block|{
name|result
operator|=
name|method
operator|.
name|getReturnType
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

