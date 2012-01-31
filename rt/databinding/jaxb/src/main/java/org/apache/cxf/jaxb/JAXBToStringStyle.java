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
name|commons
operator|.
name|lang
operator|.
name|SystemUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|ToStringBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|ToStringStyle
import|;
end_import

begin_comment
comment|/**  * Override default styles to recognise JAXBElement as needing introspection  *   * Class was moved to project org.apache.cxf.xjc-utils:cxf-xjc-runtime  * so generated code does not have to depend on cxf  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
specifier|final
class|class
name|JAXBToStringStyle
block|{
specifier|public
specifier|static
specifier|final
name|ToStringStyle
name|MULTI_LINE_STYLE
init|=
operator|new
name|JAXBToStringStyleImpl
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ToStringStyle
name|SIMPLE_STYLE
init|=
operator|new
name|JAXBToStringStyleImpl
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ToStringStyle
name|DEFAULT_STYLE
init|=
operator|new
name|JAXBToStringStyleImpl
argument_list|()
decl_stmt|;
specifier|private
name|JAXBToStringStyle
parameter_list|()
block|{
comment|//utility class
block|}
block|}
end_class

begin_class
class|class
name|JAXBToStringStyleImpl
extends|extends
name|ToStringStyle
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|584903238590491314L
decl_stmt|;
name|JAXBToStringStyleImpl
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
name|JAXBToStringStyleImpl
parameter_list|(
name|boolean
name|multiLine
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
if|if
condition|(
name|multiLine
condition|)
block|{
name|this
operator|.
name|setContentStart
argument_list|(
literal|"["
argument_list|)
expr_stmt|;
name|this
operator|.
name|setFieldSeparator
argument_list|(
name|SystemUtils
operator|.
name|LINE_SEPARATOR
operator|+
literal|"  "
argument_list|)
expr_stmt|;
name|this
operator|.
name|setFieldSeparatorAtStart
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|setContentEnd
argument_list|(
name|SystemUtils
operator|.
name|LINE_SEPARATOR
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// simple
name|this
operator|.
name|setUseClassName
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|setUseIdentityHashCode
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|setUseFieldNames
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|setContentStart
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|this
operator|.
name|setContentEnd
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Introspect into JAXBElement as a special case as it does not have a      * toString() and we loose the content      *       * @see org.apache.commons.lang.builder.ToStringStyle      */
annotation|@
name|Override
specifier|protected
name|void
name|appendDetail
parameter_list|(
name|StringBuffer
name|buffer
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|JAXBElement
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|ToStringBuilder
operator|.
name|reflectionToString
argument_list|(
name|value
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

