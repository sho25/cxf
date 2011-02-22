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
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|generators
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|CastUtils
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
name|tools
operator|.
name|common
operator|.
name|ToolConstants
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
name|tools
operator|.
name|common
operator|.
name|ToolContext
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
name|tools
operator|.
name|common
operator|.
name|ToolException
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaExceptionClass
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaField
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaModel
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
name|tools
operator|.
name|util
operator|.
name|ClassCollector
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
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|WSDLToJavaProcessor
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
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|internal
operator|.
name|ProcessorUtil
import|;
end_import

begin_class
specifier|public
class|class
name|FaultGenerator
extends|extends
name|AbstractJAXWSGenerator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FAULT_TEMPLATE
init|=
name|TEMPLATE_BASE
operator|+
literal|"/fault.vm"
decl_stmt|;
specifier|public
name|FaultGenerator
parameter_list|()
block|{
name|this
operator|.
name|name
operator|=
name|ToolConstants
operator|.
name|FAULT_GENERATOR
expr_stmt|;
block|}
specifier|public
name|boolean
name|passthrough
parameter_list|()
block|{
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_FAULT
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_ANT
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_TYPES
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_CLIENT
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_IMPL
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_SEI
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_SERVER
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_SERVICE
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|generate
parameter_list|(
name|ToolContext
name|penv
parameter_list|)
throws|throws
name|ToolException
block|{
name|this
operator|.
name|env
operator|=
name|penv
expr_stmt|;
if|if
condition|(
name|passthrough
argument_list|()
condition|)
block|{
return|return;
block|}
name|Map
argument_list|<
name|QName
argument_list|,
name|JavaModel
argument_list|>
name|map
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|penv
operator|.
name|get
argument_list|(
name|WSDLToJavaProcessor
operator|.
name|MODEL_MAP
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|JavaModel
name|javaModel
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|JavaExceptionClass
argument_list|>
name|exceptionClasses
init|=
name|javaModel
operator|.
name|getExceptionClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|exceptionClasses
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|expClassName
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|JavaExceptionClass
name|expClz
init|=
name|exceptionClasses
operator|.
name|get
argument_list|(
name|expClassName
argument_list|)
decl_stmt|;
name|clearAttributes
argument_list|()
expr_stmt|;
if|if
condition|(
name|penv
operator|.
name|containsKey
argument_list|(
name|ToolConstants
operator|.
name|CFG_USE_FQCN_FAULT_SERIAL_VERSION_UID
argument_list|)
condition|)
block|{
name|setAttributes
argument_list|(
literal|"suid"
argument_list|,
name|generateHashSUID
argument_list|(
name|expClz
operator|.
name|getFullClassName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setAttributes
argument_list|(
literal|"suid"
argument_list|,
name|generateTimestampSUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setAttributes
argument_list|(
literal|"expClass"
argument_list|,
name|expClz
argument_list|)
expr_stmt|;
name|String
name|exceptionSuperclass
init|=
literal|"Exception"
decl_stmt|;
for|for
control|(
name|JavaField
name|jf
range|:
name|expClz
operator|.
name|getFields
argument_list|()
control|)
block|{
name|String
name|jfClassName
init|=
name|jf
operator|.
name|getClassName
argument_list|()
decl_stmt|;
if|if
condition|(
name|jfClassName
operator|.
name|substring
argument_list|(
name|jfClassName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
literal|"Exception"
argument_list|)
condition|)
block|{
name|exceptionSuperclass
operator|=
literal|"java.lang.Exception"
expr_stmt|;
block|}
name|setAttributes
argument_list|(
literal|"paraName"
argument_list|,
name|ProcessorUtil
operator|.
name|mangleNameToVariableName
argument_list|(
name|jf
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expClz
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|exceptionSuperclass
argument_list|)
condition|)
block|{
name|exceptionSuperclass
operator|=
literal|"java.lang.Exception"
expr_stmt|;
block|}
name|setAttributes
argument_list|(
literal|"exceptionSuperclass"
argument_list|,
name|exceptionSuperclass
argument_list|)
expr_stmt|;
name|setCommonAttributes
argument_list|()
expr_stmt|;
name|doWrite
argument_list|(
name|FAULT_TEMPLATE
argument_list|,
name|parseOutputName
argument_list|(
name|expClz
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|expClz
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|generateHashSUID
parameter_list|(
name|String
name|className
parameter_list|)
block|{
return|return
name|Long
operator|.
name|toString
argument_list|(
name|className
operator|.
name|hashCode
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|generateTimestampSUID
parameter_list|()
block|{
return|return
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMddHHmmss"
argument_list|)
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|register
parameter_list|(
specifier|final
name|ClassCollector
name|collector
parameter_list|,
name|String
name|packageName
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
name|collector
operator|.
name|addExceptionClassName
argument_list|(
name|packageName
argument_list|,
name|fileName
argument_list|,
name|packageName
operator|+
literal|"."
operator|+
name|fileName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

