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
name|javascript
operator|.
name|fortest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|annotation
operator|.
name|XmlElement
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
name|annotation
operator|.
name|XmlRootElement
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
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_comment
comment|/**  * Bean with a selection of elements suitable for testing the JavaScript client.  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|namespace
operator|=
literal|"uri:org.apache.cxf.javascript.testns"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|namespace
operator|=
literal|"uri:org.apache.cxf.javascript.testns"
argument_list|)
specifier|public
class|class
name|TestBean1
block|{
specifier|public
name|TestBean1
parameter_list|()
block|{
name|intItem
operator|=
literal|43
expr_stmt|;
name|doubleItem
operator|=
operator|-
literal|1.0
expr_stmt|;
name|beanTwoItem
operator|=
operator|new
name|TestBean2
argument_list|(
literal|"required=true"
argument_list|)
expr_stmt|;
name|beanTwoNotRequiredItem
operator|=
literal|null
expr_stmt|;
name|enumeration
operator|=
name|AnEnum
operator|.
name|Animal
expr_stmt|;
block|}
comment|//CHECKSTYLE:OFF
specifier|public
name|String
name|stringItem
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|namespace
operator|=
literal|"uri:org.apache.cxf.javascript.testns2"
argument_list|)
specifier|public
name|int
name|intItem
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|defaultValue
operator|=
literal|"43"
argument_list|)
specifier|public
name|long
name|longItem
decl_stmt|;
specifier|public
name|byte
index|[]
name|base64Item
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|)
specifier|public
name|int
name|optionalIntItem
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|namespace
operator|=
literal|"uri:org.apache.cxf.javascript.testns2"
argument_list|)
specifier|public
name|String
name|optionalStringItem
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|)
specifier|public
name|int
index|[]
name|optionalIntArrayItem
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|defaultValue
operator|=
literal|"-1.0"
argument_list|)
specifier|public
name|double
name|doubleItem
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
specifier|public
name|TestBean2
name|beanTwoItem
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|)
specifier|public
name|TestBean2
name|beanTwoNotRequiredItem
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|defaultValue
operator|=
literal|"Animal"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
specifier|public
name|AnEnum
name|enumeration
decl_stmt|;
annotation|@
name|XmlElement
specifier|public
name|AnEnum
name|enum2
decl_stmt|;
comment|//CHECKSTYLE:ON
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|TestBean1
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|TestBean1
name|other
init|=
operator|(
name|TestBean1
operator|)
name|obj
decl_stmt|;
name|boolean
name|equalSoFar
init|=
name|intItem
operator|==
name|other
operator|.
name|intItem
operator|&&
name|longItem
operator|==
name|other
operator|.
name|longItem
operator|&&
name|optionalIntItem
operator|==
name|other
operator|.
name|optionalIntItem
operator|&&
name|doubleItem
operator|==
name|other
operator|.
name|doubleItem
operator|&&
name|beanTwoItem
operator|.
name|equals
argument_list|(
name|other
operator|.
name|beanTwoItem
argument_list|)
operator|&&
name|enumeration
operator|==
name|other
operator|.
name|enumeration
decl_stmt|;
if|if
condition|(
operator|!
name|equalSoFar
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
literal|null
operator|==
name|base64Item
condition|)
block|{
if|if
condition|(
literal|null
operator|!=
name|other
operator|.
name|base64Item
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|base64Item
argument_list|,
name|other
operator|.
name|base64Item
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
literal|null
operator|==
name|stringItem
condition|)
block|{
if|if
condition|(
literal|null
operator|!=
name|other
operator|.
name|stringItem
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|stringItem
operator|.
name|equals
argument_list|(
name|other
operator|.
name|stringItem
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
literal|null
operator|==
name|optionalIntArrayItem
condition|)
block|{
if|if
condition|(
literal|null
operator|!=
name|other
operator|.
name|optionalIntArrayItem
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|optionalIntArrayItem
argument_list|,
name|other
operator|.
name|optionalIntArrayItem
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// decisions are simpler for the last one.
if|if
condition|(
literal|null
operator|==
name|beanTwoNotRequiredItem
condition|)
block|{
return|return
name|other
operator|.
name|beanTwoNotRequiredItem
operator|==
literal|null
return|;
block|}
return|return
name|beanTwoNotRequiredItem
operator|.
name|equals
argument_list|(
name|other
operator|.
name|beanTwoNotRequiredItem
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// intentionally stupid. We don't use this object in collections.
return|return
name|super
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|256
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"TestBean1"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|" stringItem "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|stringItem
operator|==
literal|null
condition|?
literal|"Null"
else|:
name|stringItem
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|" intItem "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|intItem
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|" longItem "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|longItem
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|" base64Item "
argument_list|)
expr_stmt|;
if|if
condition|(
name|base64Item
operator|==
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"Null"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|byte
name|b
range|:
name|base64Item
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toHexString
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|builder
operator|.
name|append
argument_list|(
literal|" optionalIntItem "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|optionalIntItem
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|" optionalIntArrayItem "
argument_list|)
expr_stmt|;
if|if
condition|(
name|optionalIntArrayItem
operator|==
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"Null"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|int
name|i
range|:
name|optionalIntArrayItem
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
name|builder
operator|.
name|append
argument_list|(
literal|" beanTwoItem "
argument_list|)
expr_stmt|;
if|if
condition|(
name|beanTwoItem
operator|==
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"Null"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|beanTwoItem
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|" beanTwoNotRequiredItem "
argument_list|)
expr_stmt|;
if|if
condition|(
name|beanTwoNotRequiredItem
operator|==
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"Null"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|beanTwoNotRequiredItem
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|enumeration
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

