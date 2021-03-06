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
name|processor
operator|.
name|internal
operator|.
name|annotator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|Action
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
name|FaultAction
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
name|StringUtils
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
name|AbstractMessageContainer
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
name|FaultInfo
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
name|MessageInfo
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
name|OperationInfo
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
name|Annotator
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
name|JAnnotation
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
name|JAnnotationElement
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
name|JavaAnnotatable
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
name|JavaException
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
name|JavaInterface
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
name|JavaMethod
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|WSActionAnnotator
implements|implements
name|Annotator
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|WSAW_ACTION_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2006/05/addressing/wsdl"
argument_list|,
literal|"Action"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|WSAM_ACTION_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2007/05/addressing/metadata"
argument_list|,
literal|"Action"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|WSAW_OLD_ACTION_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2005/02/addressing/wsdl"
argument_list|,
literal|"Action"
argument_list|)
decl_stmt|;
specifier|private
name|OperationInfo
name|operation
decl_stmt|;
specifier|public
name|WSActionAnnotator
parameter_list|(
specifier|final
name|OperationInfo
name|op
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|op
expr_stmt|;
block|}
specifier|private
name|String
name|getAction
parameter_list|(
name|AbstractMessageContainer
name|mi
parameter_list|)
block|{
name|String
name|action
init|=
operator|(
name|String
operator|)
name|mi
operator|.
name|getExtensionAttribute
argument_list|(
name|WSAW_ACTION_QNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
name|action
operator|=
operator|(
name|String
operator|)
name|mi
operator|.
name|getExtensionAttribute
argument_list|(
name|WSAM_ACTION_QNAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|action
operator|==
literal|null
condition|)
block|{
name|action
operator|=
operator|(
name|String
operator|)
name|mi
operator|.
name|getExtensionAttribute
argument_list|(
name|WSAW_OLD_ACTION_QNAME
argument_list|)
expr_stmt|;
block|}
return|return
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|action
argument_list|)
condition|?
literal|null
else|:
name|action
return|;
block|}
specifier|public
name|void
name|annotate
parameter_list|(
name|JavaAnnotatable
name|ja
parameter_list|)
block|{
name|JavaMethod
name|method
decl_stmt|;
if|if
condition|(
name|ja
operator|instanceof
name|JavaMethod
condition|)
block|{
name|method
operator|=
operator|(
name|JavaMethod
operator|)
name|ja
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Action can only annotate JavaMethod"
argument_list|)
throw|;
block|}
name|boolean
name|required
init|=
literal|false
decl_stmt|;
name|JavaInterface
name|intf
init|=
name|method
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|MessageInfo
name|inputMessage
init|=
name|operation
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|MessageInfo
name|outputMessage
init|=
name|operation
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|JAnnotation
name|actionAnnotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|Action
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|inputMessage
operator|.
name|getExtensionAttributes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|inputAction
init|=
name|getAction
argument_list|(
name|inputMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|inputAction
operator|!=
literal|null
condition|)
block|{
name|actionAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"input"
argument_list|,
name|inputAction
argument_list|)
argument_list|)
expr_stmt|;
name|required
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|outputMessage
operator|!=
literal|null
operator|&&
name|outputMessage
operator|.
name|getExtensionAttributes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|outputAction
init|=
name|getAction
argument_list|(
name|outputMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|outputAction
operator|!=
literal|null
condition|)
block|{
name|actionAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"output"
argument_list|,
name|outputAction
argument_list|)
argument_list|)
expr_stmt|;
name|required
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|operation
operator|.
name|hasFaults
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|JAnnotation
argument_list|>
name|faultAnnotations
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|FaultInfo
name|faultInfo
range|:
name|operation
operator|.
name|getFaults
argument_list|()
control|)
block|{
if|if
condition|(
name|faultInfo
operator|.
name|getExtensionAttributes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|faultAction
init|=
name|getAction
argument_list|(
name|faultInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|faultAction
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|JavaException
name|exceptionClass
init|=
name|getExceptionClass
argument_list|(
name|method
argument_list|,
name|faultInfo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|exceptionClass
operator|.
name|getPackageName
argument_list|()
argument_list|)
operator|&&
operator|!
name|exceptionClass
operator|.
name|getPackageName
argument_list|()
operator|.
name|equals
argument_list|(
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|)
condition|)
block|{
name|intf
operator|.
name|addImport
argument_list|(
name|exceptionClass
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JAnnotation
name|faultAnnotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|FaultAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|faultAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"className"
argument_list|,
name|exceptionClass
argument_list|)
argument_list|)
expr_stmt|;
name|faultAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"value"
argument_list|,
name|faultAction
argument_list|)
argument_list|)
expr_stmt|;
name|faultAnnotations
operator|.
name|add
argument_list|(
name|faultAnnotation
argument_list|)
expr_stmt|;
name|required
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|actionAnnotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"fault"
argument_list|,
name|faultAnnotations
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|required
condition|)
block|{
name|method
operator|.
name|addAnnotation
argument_list|(
literal|"Action"
argument_list|,
name|actionAnnotation
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|JavaException
name|getExceptionClass
parameter_list|(
name|JavaMethod
name|method
parameter_list|,
name|FaultInfo
name|faultInfo
parameter_list|)
block|{
for|for
control|(
name|JavaException
name|exception
range|:
name|method
operator|.
name|getExceptions
argument_list|()
control|)
block|{
name|QName
name|faultName
init|=
name|faultInfo
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|exception
operator|.
name|getTargetNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|faultName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
name|exception
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
name|faultName
operator|.
name|getLocalPart
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|exception
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

